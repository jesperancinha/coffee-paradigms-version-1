package com.steelzack.coffee.system.manager;

import com.steelzack.coffee.system.input.CoffeeMachines.CoffeMachine.Coffees.Coffee;
import com.steelzack.coffee.system.input.CoffeeMachines.CoffeMachine.PaymentTypes.Payment;
import com.steelzack.coffee.system.input.Employees.Employee;
import com.steelzack.coffee.system.input.Employees.Employee.Actions.PostAction;
import com.steelzack.coffee.system.input.Employees.Employee.Actions.PreAction;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 30-04-16.
 */
@Accessors(chain = true)
@Getter
@Service
public class MachineProcessorImpl implements MachineProcessor {

    @Autowired
    private PreProcessor preProcessor;

    @Autowired
    private CoffeeProcessor coffeeProcessor;

    @Autowired
    private PaymentProcessor paymentProcessor;

    @Autowired
    private PostProcessor postProcessor;

    @Override
    public void callPreActions(Employee employee, String name, List<PreAction> preActions, Coffee coffee, Payment payment, List<PostAction> postActions) {
        preProcessor.callPreActions(employee, name, preActions, coffee, payment, postActions);
    }

    @Override
    public void callMakeCoffee(Employee employee, String name, Coffee coffee, Payment payment, List<PostAction> postActions) {
        coffeeProcessor.callMakeCoffee(employee, name, coffee, payment, postActions);
    }

    @Override
    public void callPayCoffee(String name) {
        paymentProcessor.callPayCoffee(name);
    }

    @Override
    public void callPostActions(String name) {
        postProcessor.callPostActions(name);
    }

    @Override
    public void initAll() {
        preProcessor.initExecutors();
        coffeeProcessor.initExecutors();
        paymentProcessor.initExecutors();
        postProcessor.initExecutors();
    }
}
