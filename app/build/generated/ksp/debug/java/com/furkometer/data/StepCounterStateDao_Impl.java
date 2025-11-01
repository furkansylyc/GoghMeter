package com.furkometer.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StepCounterStateDao_Impl implements StepCounterStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StepCounterState> __insertionAdapterOfStepCounterState;

  private final EntityDeletionOrUpdateAdapter<StepCounterState> __updateAdapterOfStepCounterState;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStats;

  private final SharedSQLiteStatement __preparedStmtOfUpdateInitialStepCount;

  private final SharedSQLiteStatement __preparedStmtOfReset;

  public StepCounterStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStepCounterState = new EntityInsertionAdapter<StepCounterState>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `step_counter_state` (`id`,`initialStepCount`,`hasInitialValue`,`currentSteps`,`distance`,`calories`,`lastResetDate`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StepCounterState entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getInitialStepCount());
        final int _tmp = entity.getHasInitialValue() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getCurrentSteps());
        statement.bindDouble(5, entity.getDistance());
        statement.bindLong(6, entity.getCalories());
        statement.bindLong(7, entity.getLastResetDate());
      }
    };
    this.__updateAdapterOfStepCounterState = new EntityDeletionOrUpdateAdapter<StepCounterState>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `step_counter_state` SET `id` = ?,`initialStepCount` = ?,`hasInitialValue` = ?,`currentSteps` = ?,`distance` = ?,`calories` = ?,`lastResetDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StepCounterState entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getInitialStepCount());
        final int _tmp = entity.getHasInitialValue() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getCurrentSteps());
        statement.bindDouble(5, entity.getDistance());
        statement.bindLong(6, entity.getCalories());
        statement.bindLong(7, entity.getLastResetDate());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStats = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE step_counter_state SET currentSteps = ?, distance = ?, calories = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateInitialStepCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE step_counter_state SET initialStepCount = ?, hasInitialValue = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfReset = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE step_counter_state SET initialStepCount = 0, hasInitialValue = 0, currentSteps = 0, distance = 0.0, calories = 0, lastResetDate = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object insertState(final StepCounterState state,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStepCounterState.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateState(final StepCounterState state,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStepCounterState.handle(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStats(final int steps, final double distance, final int calories,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStats.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, steps);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, distance);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, calories);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStats.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateInitialStepCount(final int count, final boolean hasValue,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateInitialStepCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        final int _tmp = hasValue ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateInitialStepCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object reset(final long resetDate, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfReset.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, resetDate);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfReset.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<StepCounterState> getState() {
    final String _sql = "SELECT * FROM step_counter_state WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"step_counter_state"}, new Callable<StepCounterState>() {
      @Override
      @Nullable
      public StepCounterState call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfInitialStepCount = CursorUtil.getColumnIndexOrThrow(_cursor, "initialStepCount");
          final int _cursorIndexOfHasInitialValue = CursorUtil.getColumnIndexOrThrow(_cursor, "hasInitialValue");
          final int _cursorIndexOfCurrentSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSteps");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfLastResetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastResetDate");
          final StepCounterState _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpInitialStepCount;
            _tmpInitialStepCount = _cursor.getInt(_cursorIndexOfInitialStepCount);
            final boolean _tmpHasInitialValue;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasInitialValue);
            _tmpHasInitialValue = _tmp != 0;
            final int _tmpCurrentSteps;
            _tmpCurrentSteps = _cursor.getInt(_cursorIndexOfCurrentSteps);
            final double _tmpDistance;
            _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            final int _tmpCalories;
            _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            final long _tmpLastResetDate;
            _tmpLastResetDate = _cursor.getLong(_cursorIndexOfLastResetDate);
            _result = new StepCounterState(_tmpId,_tmpInitialStepCount,_tmpHasInitialValue,_tmpCurrentSteps,_tmpDistance,_tmpCalories,_tmpLastResetDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStateSync(final Continuation<? super StepCounterState> $completion) {
    final String _sql = "SELECT * FROM step_counter_state WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StepCounterState>() {
      @Override
      @Nullable
      public StepCounterState call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfInitialStepCount = CursorUtil.getColumnIndexOrThrow(_cursor, "initialStepCount");
          final int _cursorIndexOfHasInitialValue = CursorUtil.getColumnIndexOrThrow(_cursor, "hasInitialValue");
          final int _cursorIndexOfCurrentSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSteps");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfLastResetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastResetDate");
          final StepCounterState _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpInitialStepCount;
            _tmpInitialStepCount = _cursor.getInt(_cursorIndexOfInitialStepCount);
            final boolean _tmpHasInitialValue;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasInitialValue);
            _tmpHasInitialValue = _tmp != 0;
            final int _tmpCurrentSteps;
            _tmpCurrentSteps = _cursor.getInt(_cursorIndexOfCurrentSteps);
            final double _tmpDistance;
            _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            final int _tmpCalories;
            _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            final long _tmpLastResetDate;
            _tmpLastResetDate = _cursor.getLong(_cursorIndexOfLastResetDate);
            _result = new StepCounterState(_tmpId,_tmpInitialStepCount,_tmpHasInitialValue,_tmpCurrentSteps,_tmpDistance,_tmpCalories,_tmpLastResetDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
