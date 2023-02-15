package com.devsuperior.hrpayroll.services;

import com.devsuperior.hrpayroll.entities.Payment;
import com.devsuperior.hrpayroll.entities.Worker;
import com.devsuperior.hrpayroll.feignclient.WorkerFeignclient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private WorkerFeignclient workerFeignclient;

    public Payment getPayment(Long workerId, int daysWorked){

        Worker worker = workerFeignclient.findWorkerById(workerId).getBody();
        return new Payment(worker.getName(), worker.getDailyIncome(), daysWorked);
    }
}
