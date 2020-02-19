package com.shun.kettle.core;

import lombok.Data;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.engine.api.model.Transformation;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import java.util.Map;


@Data
public class KettleRepository {

    private LogLevel logLevel;

    private Repository repository;

    public void runJob(String jobName, Map<String, String> parameterValues) throws KettleException {
        RepositoryDirectoryInterface directory = repository.loadRepositoryDirectoryTree().findDirectory("/");
        ObjectId objectId = repository.getJobId(jobName, directory);
        JobMeta jobMeta = repository.loadJob(objectId, null);
        Job job = new Job(repository, jobMeta);
        // 初始化job
        job.initializeVariablesFrom(null);
        job.getJobMeta().setInternalKettleVariables(job);
        job.copyParametersFrom(job.getJobMeta());
        // 设置参数
        if (parameterValues != null) {
            for (String parameterName : parameterValues.keySet()) {
                job.setParameterValue(parameterName, parameterValues.get(parameterName));
            }
        }
        // 启用参数
        job.activateParameters();
        job.setLogLevel(this.logLevel);
        job.execute(0, null);
        job.waitUntilFinished();
        job.setFinished(true);
    }

    public void runTransformation(String transName, Map<String, String> parameterValues) throws KettleException {
        RepositoryDirectoryInterface directory = repository.loadRepositoryDirectoryTree().findDirectory("/");
        ObjectId objectId = repository.getTransformationID(transName, directory);
        TransMeta transMeta = repository.loadTransformation(objectId, null);
        Trans trans = new Trans(transMeta);
        if (parameterValues != null) {
            for (String variableName : parameterValues.keySet()) {
                trans.setParameterValue(variableName, parameterValues.get(variableName));
            }
            trans.activateParameters();
        }
        trans.execute(null);
        trans.waitUntilFinished();
    }

}
