package com.jesperancinha.coffee.system.api.manager;

import com.jesperancinha.coffee.system.api.concurrency.QueueCallable;
import org.jesperancinha.coffee.system.input.Employees;
import org.jesperancinha.coffee.system.input.Employees.Employee.Actions;

import java.util.List;

public interface PostProcessor extends Processor {

    void callPostActions(Employees.Employee employee, String name, List<Actions.PostAction> postActions, QueueCallable parentCallable);
}
