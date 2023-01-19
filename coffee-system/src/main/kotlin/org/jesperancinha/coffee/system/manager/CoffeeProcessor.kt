package org.jesperancinha.coffee.system.manager

import org.jesperancinha.coffee.system.api.concurrency.QueueCallable
import org.jesperancinha.coffee.system.concurrency.CoffeeMainCallable
import org.jesperancinha.coffee.system.input.CoffeeMachines.CoffeeMachine.Coffees.Coffee
import org.jesperancinha.coffee.system.input.CoffeeMachines.CoffeeMachine.PaymentTypes.Payment
import org.jesperancinha.coffee.system.input.Employees.Employee
import org.jesperancinha.coffee.system.input.Employees.Employee.Actions.PostAction
import org.jesperancinha.coffee.system.queues.Queue
import org.jesperancinha.coffee.system.queues.QueueCofeeImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.Callable

@Service
class CoffeeProcessor(
    @Autowired
    private val queueCofee: QueueCofeeImpl,
    @Autowired
    private val paymentProcessor: PaymentProcessor
) : ProcessorAbstract() {

    fun callMakeCoffee(
        employee: Employee,
        name: String,
        coffee: Coffee,
        payment: Payment,
        postActions: List<PostAction>,
        parentCallable: QueueCallable?
    ) {
        val coffeeMainCallable = CoffeeMainCallable(employee, name, coffee, payment, postActions, paymentProcessor)
        parentCallable?.allCallables?.add(coffeeMainCallable)
    }

    override val executorServiceQueue: Queue = queueCofee

    override fun getExecutorName(callable: Callable<Boolean>): String = (callable as CoffeeMainCallable).name

    fun addQueueSize(queueSize: Int, name: String) {
        queueCofee.setQueueSize(queueSize, name)
    }

    fun initExecutors() {
        queueCofee.initExecutors()
    }

    fun stopExecutors() {
        queueCofee.stopExecutors()
    }
}