package com.steelzack.coffee.system.manager;

import com.steelzack.coffee.system.concurrency.PaymentCallableImpl;
import com.steelzack.coffee.system.input.CoffeeMachines.CoffeMachine.PaymentTypes.Payment;
import com.steelzack.coffee.system.queues.QueueAbstract;
import com.steelzack.coffee.system.queues.QueuePaymentImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by joaofilipesabinoesperancinha on 30-04-16.
 */
@Accessors(chain = true)
@Getter
@Service
public class PaymentProcessorImpl extends ProcessorAbstract implements PaymentProcessor {

    private static final Logger logger = Logger.getLogger(PaymentProcessorImpl.class);

    @Autowired
    private QueuePaymentImpl queuePayment;

    private Payment chosenPayment;

    @Override
    public void setChosenPayment(Payment chosenPayment) {
        this.chosenPayment = chosenPayment;
    }

    @Override
    public void callPayCoffee(String name) {
        final ExecutorService executor = queuePayment.getExecutor(name);
        allResults.add(executor.submit(new PaymentCallableImpl(chosenPayment, name)));
    }

    @Override
    public QueueAbstract getExecutorService() {
        return queuePayment;
    }

    @Override
    public String getExecutorName(Callable<Boolean> callable) {
        return ((PaymentCallableImpl)callable).getName();
    }

    @Override
    public void addQueueSize(int queueSize, String name) {
        queuePayment.setQueueSize(queueSize, name);
    }

    @Override
    public void initExecutors() {
        queuePayment.initExecutors();
    }

    @Override
    public void stopExectutors() {
        queuePayment.stopExecutors();
    }
}
