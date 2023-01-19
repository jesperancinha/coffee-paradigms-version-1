package org.jesperancinha.coffee.system.manager

import lombok.Getter
import lombok.experimental.Accessors
import org.jesperancinha.coffee.system.api.concurrency.QueueCallable
import org.jesperancinha.coffee.system.concurrency.CoffeeMainCallableImpl
import org.jesperancinha.coffee.system.input.CoffeeMachines.CoffeMachine.Coffees.Coffee
import org.jesperancinha.coffee.system.input.CoffeeMachines.CoffeMachine.PaymentTypes.Payment
import org.jesperancinha.coffee.system.input.Employees.Employee
import org.jesperancinha.coffee.system.input.Employees.Employee.Actions.PostAction
import org.jesperancinha.coffee.system.queues.Queue
import org.jesperancinha.coffee.system.queues.QueueCofeeImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.Callable

@Accessors(chain = true)
@Getter
@Service
abstract class CoffeeProcessorImpl(
    @Autowired
    private val queueCofee: QueueCofeeImpl,
    @Autowired
    private val machineProcessor: MachineProcessorImpl? = null
) : ProcessorAbstract() {

    fun callMakeCoffee(
        employee: Employee?,
        name: String?,
        coffee: Coffee?,
        payment: Payment?,
        postActions: List<PostAction>?,
        parentCallable: QueueCallable?
    ) {
        val coffeCallable = CoffeeMainCallableImpl(employee, name, coffee, payment, postActions, machineProcessor)
        parentCallable?.allCallables?.add(coffeCallable)
    }

    override val executorServiceQueue: Queue = queueCofee

    override fun getExecutorName(callable: Callable<Boolean>): String = (callable as CoffeeMainCallableImpl).name

    fun addQueueSize(queueSize: Int, name: String) {
        queueCofee.setQueueSize(queueSize, name)
    }

    fun initExecutors() {
        queueCofee.initExecutors()
    }

    fun stopExectutors() {
        queueCofee.stopExecutors()
    }

    abstract override fun waitForAllCalls(queueCallable: QueueCallable)
    abstract override fun runAllCalls(queueCallable: QueueCallable)

    companion object {
        private val logger = LoggerFactory.getLogger(CoffeeProcessorImpl::class.java)
    }
}