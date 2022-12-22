package com.comtom.aibo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.comtom.aibo.utils.TermsDetailsConverent;
import java.util.List;

import com.comtom.aibo.data.TermDb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TERM_DB".
*/
public class TermDbDao extends AbstractDao<TermDb, Long> {

    public static final String TABLENAME = "TERM_DB";

    /**
     * Properties of entity TermDb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TmId = new Property(0, Long.class, "tmId", true, "_id");
        public final static Property Name = new Property(1, String.class, "Name", false, "NAME");
        public final static Property Volume = new Property(2, int.class, "volume", false, "VOLUME");
        public final static Property TermsList = new Property(3, String.class, "termsList", false, "TERMS_LIST");
    }

    private final TermsDetailsConverent termsListConverter = new TermsDetailsConverent();

    public TermDbDao(DaoConfig config) {
        super(config);
    }
    
    public TermDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TERM_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: tmId
                "\"NAME\" TEXT," + // 1: Name
                "\"VOLUME\" INTEGER NOT NULL ," + // 2: volume
                "\"TERMS_LIST\" TEXT);"); // 3: termsList
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TERM_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TermDb entity) {
        stmt.clearBindings();
 
        Long tmId = entity.getTmId();
        if (tmId != null) {
            stmt.bindLong(1, tmId);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(2, Name);
        }
        stmt.bindLong(3, entity.getVolume());
 
        List termsList = entity.getTermsList();
        if (termsList != null) {
            stmt.bindString(4, termsListConverter.convertToDatabaseValue(termsList));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TermDb entity) {
        stmt.clearBindings();
 
        Long tmId = entity.getTmId();
        if (tmId != null) {
            stmt.bindLong(1, tmId);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(2, Name);
        }
        stmt.bindLong(3, entity.getVolume());
 
        List termsList = entity.getTermsList();
        if (termsList != null) {
            stmt.bindString(4, termsListConverter.convertToDatabaseValue(termsList));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TermDb readEntity(Cursor cursor, int offset) {
        TermDb entity = new TermDb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // tmId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Name
            cursor.getInt(offset + 2), // volume
            cursor.isNull(offset + 3) ? null : termsListConverter.convertToEntityProperty(cursor.getString(offset + 3)) // termsList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TermDb entity, int offset) {
        entity.setTmId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setVolume(cursor.getInt(offset + 2));
        entity.setTermsList(cursor.isNull(offset + 3) ? null : termsListConverter.convertToEntityProperty(cursor.getString(offset + 3)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TermDb entity, long rowId) {
        entity.setTmId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TermDb entity) {
        if(entity != null) {
            return entity.getTmId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TermDb entity) {
        return entity.getTmId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
