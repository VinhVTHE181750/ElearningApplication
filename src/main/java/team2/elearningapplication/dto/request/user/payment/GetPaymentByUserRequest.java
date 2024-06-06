package team2.elearningapplication.dto.request.user.payment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetPaymentByUserRequest {
    @NotBlank
    private String username;
}
