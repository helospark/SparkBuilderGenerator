package com.helospark.spark.builder.handlers;

/**
 * Marker interface for all stateful beans.
 * WARNING: Be careful with these in multithreaded environment.
 * @author helospark
 */
public interface StatefulBean {

    public void clearState();

}
