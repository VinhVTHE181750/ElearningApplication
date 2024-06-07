package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.config.VnPayConfig;
import team2.elearningapplication.dto.common.PaymentRes;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.payment.GetPaymentByUserRequest;
import team2.elearningapplication.dto.response.admin.GetTotalRevenueResponse;
import team2.elearningapplication.dto.response.user.payment.GetPaymentByUserResponse;
import team2.elearningapplication.dto.response.user.payment.ResponsePayment;
import team2.elearningapplication.entity.Payment;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IPaymentRepository;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.service.IPaymentService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;
    private final IUserRepository userRepository;

    @Override
    public ResponseCommon<PaymentRes> addPayment(double amount) throws UnsupportedEncodingException {

        String vnpTxnRef = VnPayConfig.getRandomNumber(8);
        String vnpTmnCode = VnPayConfig.vnp_TmnCode;
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        Long lastAmount = (long) (100L * amount);

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Amount", String.valueOf(lastAmount));
        vnpParams.put("vnp_Command", VnPayConfig.vnp_Command);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);
        vnpParams.put("vnp_IpAddr", "127.0.0.1");
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_OrderInfo", "Payment" + vnpTxnRef);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_Version", VnPayConfig.vnp_Version);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnpSecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentRes paymentRes = new PaymentRes();
        paymentRes.setStatus("Done");
        paymentRes.setMessage("Successfully");
        paymentRes.setUrl(paymentUrl);
        paymentRes.setVnp_TxnRef(vnpTxnRef);

        return new ResponseCommon<>(ResponseCode.SUCCESS, paymentRes);
    }

    @Override
    public ResponseCommon<GetTotalRevenueResponse> getTotalRevenue() {
        try {
            double total = 0;
            List<Payment> paymentList = paymentRepository.findAll();

            for (Payment payment : paymentList) {
                total += payment.getAmount();
            }

            GetTotalRevenueResponse response = new GetTotalRevenueResponse();
            response.setTotalRevenue(total);
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<ResponsePayment> getPaymentByUser(GetPaymentByUserRequest getPaymentByUserRequest) {
        try {
            User user = userRepository.findByUsername(getPaymentByUserRequest.getUsername()).orElse(null);
            List<Payment> paymentList = paymentRepository.findPaymentByUser(user);
            ResponsePayment responsePayment = new ResponsePayment();
            List<GetPaymentByUserResponse> getPaymentByUserResponses = paymentList.stream()
                    .map(payment -> {
                        GetPaymentByUserResponse response = new GetPaymentByUserResponse();
                        response.setCreatedAt(payment.getCreated_at());
                        response.setStatus(String.valueOf(payment.getEnumPaymentProcess()));
                        response.setAmount(payment.getAmount());
                        response.setCourseName(payment.getCourse().getName());
                        return response;
                    })
                    .toList(); // Updated to Stream.toList()
            responsePayment.setListPayment(getPaymentByUserResponses);
            return new ResponseCommon<>(ResponseCode.SUCCESS, responsePayment);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<ResponsePayment> getAllPayment() {
        try {
            List<Payment> paymentList = paymentRepository.findAll();
            ResponsePayment responsePayment = new ResponsePayment();
            List<GetPaymentByUserResponse> getPaymentByUserResponses = paymentList.stream()
                    .map(payment -> {
                        GetPaymentByUserResponse response = new GetPaymentByUserResponse();
                        response.setCreatedAt(payment.getCreated_at());
                        response.setStatus(String.valueOf(payment.getEnumPaymentProcess()));
                        response.setAmount(payment.getAmount());
                        response.setCourseName(payment.getCourse().getName());
                        return response;
                    })
                    .toList(); // Updated to Stream.toList()
            responsePayment.setListPayment(getPaymentByUserResponses);
            return new ResponseCommon<>(ResponseCode.SUCCESS, responsePayment);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }
}
