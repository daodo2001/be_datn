package com.fpt.hotel.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    private Integer id_creator;

    private Long idBooking;

    private Double totalPrice;

    private String status;

    private String imageCmnd;

    private boolean isCostsIncurred;

    private String nameCostsIncurred;

    private Double priceCostsIncurred;

}
