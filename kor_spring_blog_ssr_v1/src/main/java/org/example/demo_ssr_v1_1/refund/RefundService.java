package org.example.demo_ssr_v1_1.refund;

import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1_1._core.errors.exception.Exception400;
import org.example.demo_ssr_v1_1._core.errors.exception.Exception403;
import org.example.demo_ssr_v1_1._core.errors.exception.Exception404;
import org.example.demo_ssr_v1_1.payment.Payment;
import org.example.demo_ssr_v1_1.payment.PaymentRepository;
import org.example.demo_ssr_v1_1.user.User;
import org.example.demo_ssr_v1_1.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefundService {

    private final RefundRequestRepository refundRequestRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public Payment 환불요청폼화면검증(Long paymentId, Long  userId) {

        // 1. 결제 내역 목록 조회 (User 정보)
        Payment payment = paymentRepository.findByIdWithUser(paymentId);
        // 2. 본인 확인
        if(!payment.getUser().getId().equals(userId)) {
            throw new Exception403("본인 결제 내역만 환불 요청할 수 있습니다.");
        }
        // 3. 결제 완료 상태인지 확인("paid" 일 때만 폼을 열어 줄 예정)
        if(!"paid".equals(payment.getStatus())) {
            throw new Exception400("결제 완료된 상태만 환불 요청할 수 있습니다.");
        }
        // 4. 이미 환불 요청한 상태인지 아닌지를
        if (refundRequestRepository.findByPaymentId(paymentId).isPresent()) {
            throw new Exception400("이미 환불 요청 진행중. 결과를 기다리시오");
        }

        return payment;
    }

    // 1 단계 : 사용자가 환불 요청 함
    @Transactional
    public void 환불요청(Long userId, RefundRequestDTO.RequestDTO reqDTO) {

        // 화면 검증 로직 재사용
        Payment payment = 환불요청폼화면검증(reqDTO.getPaymentId(), userId);

        // 사용자 조회 (세션 값으로 넘어온 id 실제 존재 하는지 확인)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));

        // 환불 요청 데이블에 이력 저장
        Refund refundRequest = Refund.builder()
                .user(user)
                .payment(payment)
                .reason(reqDTO.getReason())
                .build();

        refundRequestRepository.save(refundRequest);
    }

    public List<RefundResponse.ListDTO> 환불요청목록조회(Long userId) {

        // 내 아이디로 조회 (환불 요청 내역)
        List<Refund> refundList = refundRequestRepository.findAllByUserId(userId);

        return refundList.stream()
                .map(RefundResponse.ListDTO::new)
                .toList();
    }
}
