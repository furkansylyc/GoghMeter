package com.furkometer.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PedometerDatabase_Impl extends PedometerDatabase {
  private volatile StepRecordDao _stepRecordDao;

  private volatile StepCounterStateDao _stepCounterStateDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `step_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `steps` INTEGER NOT NULL, `distance` REAL NOT NULL, `calories` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `step_counter_state` (`id` INTEGER NOT NULL, `initialStepCount` INTEGER NOT NULL, `hasInitialValue` INTEGER NOT NULL, `currentSteps` INTEGER NOT NULL, `distance` REAL NOT NULL, `calories` INTEGER NOT NULL, `lastResetDate` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '62e45ee2d863e879a4abee419da23b11')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `step_records`");
        db.execSQL("DROP TABLE IF EXISTS `step_counter_state`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStepRecords = new HashMap<String, TableInfo.Column>(5);
        _columnsStepRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepRecords.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepRecords.put("steps", new TableInfo.Column("steps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepRecords.put("distance", new TableInfo.Column("distance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepRecords.put("calories", new TableInfo.Column("calories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStepRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStepRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStepRecords = new TableInfo("step_records", _columnsStepRecords, _foreignKeysStepRecords, _indicesStepRecords);
        final TableInfo _existingStepRecords = TableInfo.read(db, "step_records");
        if (!_infoStepRecords.equals(_existingStepRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "step_records(com.furkometer.data.StepRecord).\n"
                  + " Expected:\n" + _infoStepRecords + "\n"
                  + " Found:\n" + _existingStepRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsStepCounterState = new HashMap<String, TableInfo.Column>(7);
        _columnsStepCounterState.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepCounterState.put("initialStepCount", new TableInfo.Column("initialStepCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepCounterState.put("hasInitialValue", new TableInfo.Column("hasInitialValue", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepCounterState.put("currentSteps", new TableInfo.Column("currentSteps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepCounterState.put("distance", new TableInfo.Column("distance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepCounterState.put("calories", new TableInfo.Column("calories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStepCounterState.put("lastResetDate", new TableInfo.Column("lastResetDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStepCounterState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStepCounterState = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStepCounterState = new TableInfo("step_counter_state", _columnsStepCounterState, _foreignKeysStepCounterState, _indicesStepCounterState);
        final TableInfo _existingStepCounterState = TableInfo.read(db, "step_counter_state");
        if (!_infoStepCounterState.equals(_existingStepCounterState)) {
          return new RoomOpenHelper.ValidationResult(false, "step_counter_state(com.furkometer.data.StepCounterState).\n"
                  + " Expected:\n" + _infoStepCounterState + "\n"
                  + " Found:\n" + _existingStepCounterState);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "62e45ee2d863e879a4abee419da23b11", "f9f8f0c2501e32b2cd0b7e170c7a1eb3");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "step_records","step_counter_state");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `step_records`");
      _db.execSQL("DELETE FROM `step_counter_state`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(StepRecordDao.class, StepRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StepCounterStateDao.class, StepCounterStateDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public StepRecordDao stepRecordDao() {
    if (_stepRecordDao != null) {
      return _stepRecordDao;
    } else {
      synchronized(this) {
        if(_stepRecordDao == null) {
          _stepRecordDao = new StepRecordDao_Impl(this);
        }
        return _stepRecordDao;
      }
    }
  }

  @Override
  public StepCounterStateDao stepCounterStateDao() {
    if (_stepCounterStateDao != null) {
      return _stepCounterStateDao;
    } else {
      synchronized(this) {
        if(_stepCounterStateDao == null) {
          _stepCounterStateDao = new StepCounterStateDao_Impl(this);
        }
        return _stepCounterStateDao;
      }
    }
  }
}
