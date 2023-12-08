package com.aula5.fivespring;


import com.aula5.fivespring.exceptions.UnsupportedMatchOperationsException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {


    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();


    @RequestMapping(value = "/soma/{number}/{number2}",
    method = RequestMethod.GET)
    public Double Soma (@PathVariable(value = "number") String number,
                        @PathVariable(value = "number2") String number2
                        )throws Exception{

        if(!isNumeric(number) || !isNumeric(number2)) {

            throw new UnsupportedMatchOperationsException("Please data incorret");
        }

        return convertToDouble(number) + convertToDouble(number2);
    }

    private Double convertToDouble(String num){
        if (num==null){
            return 0D;
        }
        String number = num.replaceAll(",",".");
        if (isNumeric(number)) return Double.parseDouble(number);
        return 0D;
    }
    private boolean isNumeric(String num){
        if(num==null) return false;
            String number = num.replaceAll(",",".");


            return number.matches("[-+]?[0-9]*\\.?[0-9]+");

    }








}
