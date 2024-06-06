package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.payment.GetPaymentByUserRequest;
import team2.elearningapplication.dto.response.user.payment.ResponsePayment;
import team2.elearningapplication.service.IPaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;

    @GetMapping("/get-payment-user")
    public ResponseEntity<ResponseCommon<ResponsePayment>> getPaymentByUser(@ParameterObject GetPaymentByUserRequest getPaymentByUserRequest) {
        ResponseCommon<ResponsePayment> response = paymentService.getPaymentByUser(getPaymentByUserRequest);

        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get payment failed", null));
        }
    }

    @GetMapping("/get-all-payment")
    public ResponseEntity<ResponseCommon<ResponsePayment>> getPaymentByUser() {
        ResponseCommon<ResponsePayment> response = paymentService.getAllPayment();

        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get payment failed", null));
        }
    }

}
