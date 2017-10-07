package com.helospark.spark.builder.handlers;

/**
 * Marker interface for all stateful beans.
 * WARNING: Use these safely.
 * Implementation has to be sure, to make it threadsafe.
 * @author helospark
 */
public interface StatefulBean {

    public void clearState();
}
