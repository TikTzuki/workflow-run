// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.function.Supplier;
import org.apache.ibatis.executor.BatchExecutorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.SQLException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil
{
    public static final String PERSISTENCE_EXCEPTION_MESSAGE = "An exception occurred in the persistence layer. Please check the server logs for a detailed message and the entire exception stack trace.";
    
    public static String getExceptionStacktrace(final Throwable exception) {
        final StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
    
    public static String getExceptionStacktrace(final ByteArrayEntity byteArray) {
        String result = null;
        if (byteArray != null) {
            result = StringUtil.fromBytes(byteArray.getBytes());
        }
        return result;
    }
    
    public static ByteArrayEntity createJobExceptionByteArray(final byte[] byteArray, final ResourceType type) {
        return createExceptionByteArray("job.exceptionByteArray", byteArray, type);
    }
    
    public static ByteArrayEntity createExceptionByteArray(final String name, final byte[] byteArray, final ResourceType type) {
        ByteArrayEntity result = null;
        if (byteArray != null) {
            result = new ByteArrayEntity(name, byteArray, type);
            Context.getCommandContext().getByteArrayManager().insertByteArray(result);
        }
        return result;
    }
    
    public static boolean checkValueTooLongException(final ProcessEngineException exception) {
        final List<SQLException> sqlExceptionList = findRelatedSqlExceptions(exception);
        for (final SQLException ex : sqlExceptionList) {
            if (ex.getMessage().contains("too long") || ex.getMessage().contains("too large") || ex.getMessage().contains("TOO LARGE") || ex.getMessage().contains("ORA-01461") || ex.getMessage().contains("ORA-01401") || ex.getMessage().contains("data would be truncated") || ex.getMessage().contains("SQLCODE=-302, SQLSTATE=22001")) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkConstraintViolationException(final ProcessEngineException exception) {
        final List<SQLException> sqlExceptionList = findRelatedSqlExceptions(exception);
        for (final SQLException ex : sqlExceptionList) {
            if (ex.getMessage().contains("constraint") || ex.getMessage().contains("violat") || ex.getMessage().toLowerCase().contains("duplicate") || ex.getMessage().contains("ORA-00001") || ex.getMessage().contains("SQLCODE=-803, SQLSTATE=23505")) {
                return true;
            }
        }
        return false;
    }
    
    public static List<SQLException> findRelatedSqlExceptions(final Throwable exception) {
        final List<SQLException> sqlExceptionList = new ArrayList<SQLException>();
        Throwable cause = exception;
        do {
            if (cause instanceof SQLException) {
                SQLException sqlEx = (SQLException)cause;
                sqlExceptionList.add(sqlEx);
                while (sqlEx.getNextException() != null) {
                    sqlExceptionList.add(sqlEx.getNextException());
                    sqlEx = sqlEx.getNextException();
                }
            }
            cause = cause.getCause();
        } while (cause != null);
        return sqlExceptionList;
    }
    
    public static boolean checkForeignKeyConstraintViolation(final Throwable cause) {
        final List<SQLException> relatedSqlExceptions = findRelatedSqlExceptions(cause);
        for (final SQLException exception : relatedSqlExceptions) {
            if ("23503".equals(exception.getSQLState()) && exception.getErrorCode() == 0) {
                return false;
            }
            if (exception.getMessage().toLowerCase().contains("foreign key constraint") || ("23000".equals(exception.getSQLState()) && exception.getErrorCode() == 547) || exception.getMessage().toLowerCase().contains("foreign key constraint") || ("23000".equals(exception.getSQLState()) && exception.getErrorCode() == 1452) || exception.getMessage().toLowerCase().contains("integrity constraint") || ("23000".equals(exception.getSQLState()) && exception.getErrorCode() == 2291) || ("23506".equals(exception.getSQLState()) && exception.getErrorCode() == 23506) || (exception.getMessage().toLowerCase().contains("sqlstate=23503") && exception.getMessage().toLowerCase().contains("sqlcode=-530")) || ("23503".equals(exception.getSQLState()) && exception.getErrorCode() == -530)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkVariableIntegrityViolation(final Throwable cause) {
        final List<SQLException> relatedSqlExceptions = findRelatedSqlExceptions(cause);
        for (final SQLException exception : relatedSqlExceptions) {
            if ((exception.getMessage().toLowerCase().contains("act_uniq_variable") && "23000".equals(exception.getSQLState()) && exception.getErrorCode() == 1062) || (exception.getMessage().toLowerCase().contains("act_uniq_variable") && "23505".equals(exception.getSQLState()) && exception.getErrorCode() == 0) || (exception.getMessage().toLowerCase().contains("act_uniq_variable") && "23000".equals(exception.getSQLState()) && exception.getErrorCode() == 2601) || (exception.getMessage().toLowerCase().contains("act_uniq_variable") && "23000".equals(exception.getSQLState()) && exception.getErrorCode() == 1) || (exception.getMessage().toLowerCase().contains("act_uniq_variable") && "23505".equals(exception.getSQLState()) && exception.getErrorCode() == 23505)) {
                return true;
            }
        }
        return false;
    }
    
    public static Boolean checkCrdbTransactionRetryException(final Throwable cause) {
        final List<SQLException> relatedSqlExceptions = findRelatedSqlExceptions(cause);
        for (final SQLException exception : relatedSqlExceptions) {
            final String errorMessage = exception.getMessage().toLowerCase();
            final int errorCode = exception.getErrorCode();
            if ((errorCode == 40001 || errorMessage != null) && (errorMessage.contains("restart transaction") || errorMessage.contains("retry txn")) && !errorMessage.contains("retry_commit_deadline_exceeded")) {
                return true;
            }
        }
        return false;
    }
    
    public static BatchExecutorException findBatchExecutorException(final Throwable exception) {
        Throwable cause = exception;
        while (!(cause instanceof BatchExecutorException)) {
            cause = cause.getCause();
            if (cause == null) {
                return null;
            }
        }
        return (BatchExecutorException)cause;
    }
    
    public static String collectExceptionMessages(final Throwable cause) {
        String message = cause.getMessage();
        Throwable exCause = cause;
        do {
            if (exCause instanceof BatchExecutorException) {
                final List<SQLException> relatedSqlExceptions = findRelatedSqlExceptions(exCause);
                final StringBuilder sb = new StringBuilder();
                for (final SQLException sqlException : relatedSqlExceptions) {
                    sb.append(sqlException).append("\n");
                }
                message = message + "\n" + sb.toString();
            }
            exCause = exCause.getCause();
        } while (exCause != null);
        return message;
    }
    
    public static <T> T doWithExceptionWrapper(final Supplier<T> supplier) {
        try {
            return supplier.get();
        }
        catch (Exception ex) {
            throw wrapPersistenceException(ex);
        }
    }
    
    public static ProcessEngineException wrapPersistenceException(final Exception ex) {
        return new ProcessEngineException("An exception occurred in the persistence layer. Please check the server logs for a detailed message and the entire exception stack trace.", ex);
    }
}
