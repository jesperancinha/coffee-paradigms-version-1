package com.steelzack.coffee.system.manager;

import com.steelzack.coffee.system.queues.QueueAbstract;

/**
 * Created by joaofilipesabinoesperancinha on 05-05-16.
 */
public abstract class ProcessorImpl {

    public void setQueueSize(int queueSize)
    {
       getExecutorService().setQueueSize(queueSize);
    }

    abstract QueueAbstract getExecutorService();
}
