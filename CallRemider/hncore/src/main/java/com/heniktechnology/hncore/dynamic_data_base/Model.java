package com.heniktechnology.hncore.dynamic_data_base;

/*
 * Copyright (C) 2010 Michael Pardo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.heniktechnology.hncore.dynamic_data_base.annotation.Column;
import com.heniktechnology.hncore.dynamic_data_base.content.ContentProvider;
import com.heniktechnology.hncore.dynamic_data_base.exceptions.IllegalUniqueIdentifierException;
import com.heniktechnology.hncore.dynamic_data_base.exceptions.ModelUpdateException;
import com.heniktechnology.hncore.dynamic_data_base.query.Delete;
import com.heniktechnology.hncore.dynamic_data_base.query.Select;
import com.heniktechnology.hncore.dynamic_data_base.serializer.TypeSerializer;
import com.heniktechnology.hncore.dynamic_data_base.util.ModelUtils;
import com.heniktechnology.hncore.dynamic_data_base.util.ReflectionUtils;
import com.heniktechnology.hncore.utility.HNLoger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Model {

    /**
     * Prime number used for hashcode() implementation.
     */
    private static final int HASH_PRIME = 739;

    //////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE MEMBERS
    //////////////////////////////////////////////////////////////////////////////////////

    private Long mId = null;
    private final TableInfo mTableInfo;
    private final String idName;
    //////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    //////////////////////////////////////////////////////////////////////////////////////

    public Model() {
        mTableInfo = Cache.getTableInfo(getClass());
        idName = mTableInfo.getIdName();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    public final Long getId() {
        return mId;
    }

    public void delete() {
        Cache.openDatabase().delete(mTableInfo.getTableName(), idName + "=?", new String[]{getId().toString()});
        Cache.removeEntity(this);

        Cache.getContext().getContentResolver()
                .notifyChange(ContentProvider.createUri(mTableInfo.getType(), mId), null);
    }

    public final Long save() {
        final SQLiteDatabase db = Cache.openDatabase();
        final ContentValues values = new ContentValues();

        for (Field field : mTableInfo.getColumnFields()) {
            final String fieldName = mTableInfo.getColumnName(field);
            Class<?> fieldType = field.getType();

            field.setAccessible(true);

            try {
                Object value = field.get(this);

                if (value != null) {
                    final TypeSerializer typeSerializer = Cache.getParserForType(fieldType);
                    if (typeSerializer != null) {
                        // serialize data
                        value = typeSerializer.serialize(value);
                        // set new object type
                        if (value != null) {
                            fieldType = value.getClass();
                            // check that the serializer returned what it promised
                            if (!fieldType.equals(typeSerializer.getSerializedType())) {
                                HNLoger.info(String.format("TypeSerializer returned wrong type: expected a %s but got a %s",
                                        typeSerializer.getSerializedType(), fieldType));
                            }
                        }
                    }
                }

                // TODO: Find a smarter way to do this? This if block is necessary because we
                // can't know the type until runtime.
                if (value == null) {
                    values.putNull(fieldName);
                } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                    values.put(fieldName, (Byte) value);
                } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                    values.put(fieldName, (Short) value);
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    values.put(fieldName, (Integer) value);
                } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    values.put(fieldName, (Long) value);
                } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                    values.put(fieldName, (Float) value);
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    values.put(fieldName, (Double) value);
                } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                    values.put(fieldName, (Boolean) value);
                } else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
                    values.put(fieldName, value.toString());
                } else if (fieldType.equals(String.class)) {
                    values.put(fieldName, value.toString());
                } else if (fieldType.equals(Byte[].class) || fieldType.equals(byte[].class)) {
                    values.put(fieldName, (byte[]) value);
                } else if (ReflectionUtils.isModel(fieldType)) {
                    Model model = (Model) value;
                    Long foreignkeyID = model.getId();
                    Column.ModelAutoCreateAction autoCreateAction = field.getAnnotation(Column.class).onAutoCreate();

                    if (foreignkeyID == null && field.getAnnotation(Column.class).autoCreate()) {
                        autoCreateAction = Column.ModelAutoCreateAction.SAVE;
                    }
                    switch (autoCreateAction) {
                        case NO_ACTION:
                            break;
                        case SAVE:
                            model.save();
                            foreignkeyID = model.getId();
                            break;
                        case CREATE_OR_UPDATE:
                            foreignkeyID = createOrUpdate(model).getEntity().getId();
                            break;
                    }
                    values.put(fieldName, foreignkeyID);
                } else if (ReflectionUtils.isSubclassOf(fieldType, Enum.class)) {
                    values.put(fieldName, ((Enum<?>) value).name());
                }
            } catch (IllegalArgumentException e) {
                HNLoger.error(e, e.getClass().getName());
            } catch (IllegalAccessException e) {
                HNLoger.error(e,e.getClass().getName());
            } catch (IllegalUniqueIdentifierException e) {
                HNLoger.error(e,e.getClass().getName());
            } catch (ModelUpdateException e) {
                HNLoger.error(e,e.getClass().getName());
            }
        }

        if (mId == null) {
            mId = db.insert(mTableInfo.getTableName(), null, values);
        } else {
            db.update(mTableInfo.getTableName(), values, idName + "=" + mId, null);
        }

        Cache.getContext().getContentResolver()
                .notifyChange(ContentProvider.createUri(mTableInfo.getType(), mId), null);
        return mId;
    }

    // Convenience methods

    public static void delete(Class<? extends Model> type, long id) {
        TableInfo tableInfo = Cache.getTableInfo(type);
        new Delete().from(type).where(tableInfo.getIdColumn() + "=?", id).execute();
    }

    public static <T extends Model> T load(Class<T> type, long id) {
        TableInfo tableInfo = Cache.getTableInfo(type);
        return (T) new Select().from(type).where(tableInfo.getIdColumn() + "=?", id).executeSingle();
    }

    // Model population

    public final void loadFromCursor(Cursor cursor) {
        /**
         * Obtain the columns ordered to fix issue #106 (https://github.com/pardom/ActiveAndroid/issues/106)
         * when the cursor have multiple columns with same name obtained from join tables.
         */
        List<String> columnsOrdered = new ArrayList<String>(Arrays.asList(cursor.getColumnNames()));
        for (Field field : mTableInfo.getAllFields()) {
            final String fieldName = mTableInfo.getDatabaseName(field);
            Class<?> fieldType = field.getType();
            final int columnIndex = columnsOrdered.indexOf(fieldName);

            if (columnIndex < 0) {
                continue;
            }

            field.setAccessible(true);

            try {
                boolean columnIsNull = cursor.isNull(columnIndex);
                TypeSerializer typeSerializer = Cache.getParserForType(fieldType);
                Object value = null;

                if (typeSerializer != null) {
                    fieldType = typeSerializer.getSerializedType();
                }

                // TODO: Find a smarter way to do this? This if block is necessary because we
                // can't know the type until runtime.
                if (columnIsNull) {
                    field = null;
                } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                    value = cursor.getInt(columnIndex);
                } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                    value = cursor.getInt(columnIndex);
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    value = cursor.getInt(columnIndex);
                } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    value = cursor.getLong(columnIndex);
                } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                    value = cursor.getFloat(columnIndex);
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    value = cursor.getDouble(columnIndex);
                } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                    value = cursor.getInt(columnIndex) != 0;
                } else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
                    value = cursor.getString(columnIndex).charAt(0);
                } else if (fieldType.equals(String.class)) {
                    value = cursor.getString(columnIndex);
                } else if (fieldType.equals(Byte[].class) || fieldType.equals(byte[].class)) {
                    value = cursor.getBlob(columnIndex);
                } else if (ReflectionUtils.isModel(fieldType)) {
                    final long entityId = cursor.getLong(columnIndex);
                    final Class<? extends Model> entityType = (Class<? extends Model>) fieldType;

                    Model entity = Cache.getEntity(entityType, entityId);
                    if (entity == null) {
                        TableInfo tableInfo = Cache.getTableInfo(entityType);
                        entity = new Select().from(entityType).where(tableInfo.getIdColumn() + "=?", entityId).executeSingle();
                    }

                    value = entity;
                } else if (ReflectionUtils.isSubclassOf(fieldType, Enum.class)) {
                    @SuppressWarnings("rawtypes")
                    final Class<? extends Enum> enumType = (Class<? extends Enum>) fieldType;
                    value = Enum.valueOf(enumType, cursor.getString(columnIndex));
                }

                // Use a deserializer if one is available
                if (typeSerializer != null && !columnIsNull) {
                    value = typeSerializer.deserialize(value);
                }

                // Set the field value
                if (value != null) {
                    field.set(this, value);
                }
            } catch (IllegalArgumentException e) {
               HNLoger.error(e,e.getClass().getName());
            } catch (IllegalAccessException e) {
                HNLoger.error(e,e.getClass().getName());
            } catch (SecurityException e) {
                HNLoger.error(e,e.getClass().getName());
            }
        }

        if (mId != null) {
            Cache.addEntity(this);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    protected final <T extends Model> List<T> getMany(Class<T> type, String foreignKey) {
        return new Select().from(type).where(Cache.getTableName(type) + "." + foreignKey + "=?", getId()).execute();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // OVERRIDEN METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return mTableInfo.getTableName() + "@" + getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model && this.mId != null) {
            final Model other = (Model) obj;

            return this.mId.equals(other.mId)
                    && (this.mTableInfo.getTableName().equals(other.mTableInfo.getTableName()));
        } else {
            return this == obj;
        }
    }

    @Override
    public int hashCode() {
        int hash = HASH_PRIME;
        hash += HASH_PRIME * (mId == null ? super.hashCode() : mId.hashCode()); //if id is null, use Object.hashCode()
        hash += HASH_PRIME * mTableInfo.getTableName().hashCode();
        return hash; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Create or update a model entity. The table has to have an unique identifier registrated.
     * The default unique identifier is the id column.
     * If there are foreign keys you should wrap this method in a transaction.
     *
     * @param object
     * @return the created/updated enity
     * @throws IllegalUniqueIdentifierException
     * @throws ModelUpdateException
     */
    public static <T extends Model> ModelSaveResult<T> createOrUpdate(T object) throws IllegalUniqueIdentifierException, ModelUpdateException {
        Class<? extends Model> objectClass = object.getClass();
        TableInfo info = Cache.getTableInfo(objectClass);
        String uniqueIdentifier = info.getUniqueIdentifier();
        T entity;
        try {
            Field f = getUniqueField(objectClass, uniqueIdentifier);
            String columnName = info.getColumn(f);
            f.setAccessible(true);
            Object value = f.get(object);
            entity = new Select().from(objectClass).where(columnName + "=?", value).executeSingle();
        } catch (NoSuchFieldException e) {
            throw new IllegalUniqueIdentifierException("Couldn't get the specified unique identifier", e);
        } catch (IllegalAccessException e) {
            throw new IllegalUniqueIdentifierException("Couldn't get the specified unique identifier", e);
        }
        List<Model> modelsToBeDeleted = new ArrayList<Model>();
        boolean isNew = entity == null;
        if (!isNew) {
            modelsToBeDeleted = entity.updateWith(object);
        } else {
            entity = object;
        }
        ModelSaveResult<T> result = new ModelSaveResult<T>(entity, isNew);
        entity.save();
        for (Model m : modelsToBeDeleted) {
            m.delete();
        }
        return result;
    }

    /**
     * This method updates a model with another one.
     *
     * @param other
     * @return Entities to delete. (Because they get exchanged).
     * @throws ModelUpdateException
     * @throws IllegalUniqueIdentifierException
     */
    protected List<Model> updateWith(Model other) throws ModelUpdateException, IllegalUniqueIdentifierException {
        Class<? extends Model> myClass = getClass();
        Class<? extends Model> otherClass = other.getClass();
        ArrayList<Model> entitiesToBeDeleted = new ArrayList<Model>();
        if (myClass.isAssignableFrom(otherClass)) {
            fieldloop:
            for (Field field : Cache.getTableInfo(myClass).getColumnFields()) {
                field.setAccessible(true);
                try {
                    Object newValue = field.get(other);
                    Column annotation = field.getAnnotation(Column.class);
                    Column.ModelUpdateAction modelUpdateAction = (annotation != null) ? annotation.onModelUpdate() : Column.ModelUpdateAction.NO_ACTION;
                    switch (modelUpdateAction) {
                        case NO_ACTION:
                            continue fieldloop;
                        case REPLACE:
                            if (ModelUtils.isForeignKey(field)) {
                                Model thisFieldValue = (Model) field.get(this);
                                entitiesToBeDeleted.add(thisFieldValue);
                            }
                            break;
                        case UPDATE:
                            if (ModelUtils.isForeignKey(field)) {
                                newValue = Model.createOrUpdate((Model) newValue).getEntity();
                            }
                            break;

                    }
                    field.set(this, newValue);
                } catch (IllegalAccessException e) {
                    throw new ModelUpdateException("The update of field: " + field.getName() + "was not possible.", e);
                } catch (IllegalArgumentException e) {
                    throw new ModelUpdateException("The update of field: " + field.getName() + "was not possible.", e);
                }
            }
        }
        return entitiesToBeDeleted;
    }

    private static Field getUniqueField(Class<?> objectClass, String uniqueIdentifier) throws NoSuchFieldException {

        Field field = null;
        try {
            field = objectClass.getDeclaredField(uniqueIdentifier);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = objectClass.getSuperclass();
            if (superclass != null) {
                field = getUniqueField(superclass, uniqueIdentifier);
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(uniqueIdentifier);
        }
        return field;
    }
}