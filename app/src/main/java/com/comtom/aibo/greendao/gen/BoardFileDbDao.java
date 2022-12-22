package com.comtom.aibo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.comtom.aibo.utils.MusicConverent;
import com.comtom.aibo.utils.TermsDetailsConverent;
import java.util.List;

import com.comtom.aibo.data.BoardFileDb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOARD_FILE_DB".
*/
public class BoardFileDbDao extends AbstractDao<BoardFileDb, Long> {

    public static final String TABLENAME = "BOARD_FILE_DB";

    /**
     * Properties of entity BoardFileDb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property BoardId = new Property(0, Long.class, "boardId", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Sid = new Property(2, int.class, "Sid", false, "SID");
        public final static Property Status = new Property(3, int.class, "Status", false, "STATUS");
        public final static Property Volume = new Property(4, int.class, "volume", false, "VOLUME");
        public final static Property Type = new Property(5, int.class, "type", false, "TYPE");
        public final static Property TermsList = new Property(6, String.class, "termsList", false, "TERMS_LIST");
        public final static Property MusicList = new Property(7, String.class, "musicList", false, "MUSIC_LIST");
    }

    private final TermsDetailsConverent termsListConverter = new TermsDetailsConverent();
    private final MusicConverent musicListConverter = new MusicConverent();

    public BoardFileDbDao(DaoConfig config) {
        super(config);
    }
    
    public BoardFileDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOARD_FILE_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: boardId
                "\"NAME\" TEXT," + // 1: name
                "\"SID\" INTEGER NOT NULL ," + // 2: Sid
                "\"STATUS\" INTEGER NOT NULL ," + // 3: Status
                "\"VOLUME\" INTEGER NOT NULL ," + // 4: volume
                "\"TYPE\" INTEGER NOT NULL ," + // 5: type
                "\"TERMS_LIST\" TEXT," + // 6: termsList
                "\"MUSIC_LIST\" TEXT);"); // 7: musicList
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOARD_FILE_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BoardFileDb entity) {
        stmt.clearBindings();
 
        Long boardId = entity.getBoardId();
        if (boardId != null) {
            stmt.bindLong(1, boardId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getSid());
        stmt.bindLong(4, entity.getStatus());
        stmt.bindLong(5, entity.getVolume());
        stmt.bindLong(6, entity.getType());
 
        List termsList = entity.getTermsList();
        if (termsList != null) {
            stmt.bindString(7, termsListConverter.convertToDatabaseValue(termsList));
        }
 
        List musicList = entity.getMusicList();
        if (musicList != null) {
            stmt.bindString(8, musicListConverter.convertToDatabaseValue(musicList));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BoardFileDb entity) {
        stmt.clearBindings();
 
        Long boardId = entity.getBoardId();
        if (boardId != null) {
            stmt.bindLong(1, boardId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getSid());
        stmt.bindLong(4, entity.getStatus());
        stmt.bindLong(5, entity.getVolume());
        stmt.bindLong(6, entity.getType());
 
        List termsList = entity.getTermsList();
        if (termsList != null) {
            stmt.bindString(7, termsListConverter.convertToDatabaseValue(termsList));
        }
 
        List musicList = entity.getMusicList();
        if (musicList != null) {
            stmt.bindString(8, musicListConverter.convertToDatabaseValue(musicList));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BoardFileDb readEntity(Cursor cursor, int offset) {
        BoardFileDb entity = new BoardFileDb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // boardId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // Sid
            cursor.getInt(offset + 3), // Status
            cursor.getInt(offset + 4), // volume
            cursor.getInt(offset + 5), // type
            cursor.isNull(offset + 6) ? null : termsListConverter.convertToEntityProperty(cursor.getString(offset + 6)), // termsList
            cursor.isNull(offset + 7) ? null : musicListConverter.convertToEntityProperty(cursor.getString(offset + 7)) // musicList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BoardFileDb entity, int offset) {
        entity.setBoardId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSid(cursor.getInt(offset + 2));
        entity.setStatus(cursor.getInt(offset + 3));
        entity.setVolume(cursor.getInt(offset + 4));
        entity.setType(cursor.getInt(offset + 5));
        entity.setTermsList(cursor.isNull(offset + 6) ? null : termsListConverter.convertToEntityProperty(cursor.getString(offset + 6)));
        entity.setMusicList(cursor.isNull(offset + 7) ? null : musicListConverter.convertToEntityProperty(cursor.getString(offset + 7)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BoardFileDb entity, long rowId) {
        entity.setBoardId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BoardFileDb entity) {
        if(entity != null) {
            return entity.getBoardId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BoardFileDb entity) {
        return entity.getBoardId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}