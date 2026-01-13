package org.example.demo_ssr_v1_1.refund;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.demo_ssr_v1_1._core.utils.MyDateUtil;
import org.example.demo_ssr_v1_1.user.User;

public class RefundResponse {

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ListDTO {
        private Long id; // 환불 요청 테이블 pk
        private Long paymentId;
        private Integer amount;
        private String reason; // 본인이 작성한 환불 사유
        private String rejectReason; // 관리자가 환불 거절 사유, (승인이 필요 없음)
        private String statusDisplay; // 화면 표시용 (대기중, 승인, 거절)

        // 상태별 플래그 변수 사용
        private boolean inPending; // 대기중
        private boolean isApproved;
        private boolean isRejected;

        private String username;
        private String impUid;
        private String merchantUid;
        private RefundStatus status;
        private String requestAt;

        public ListDTO(Refund refund) {
            this.id = refund.getId();
            this.paymentId = refund.getPayment().getId();
            this.amount = refund.getPayment().getAmount();
            this.reason = refund.getReason();
            this.rejectReason = refund.getRejectReason() == null ? "" :refund.getRejectReason();

            // 스위치 표현식 (14 버전 이후부터 사용가능) (ARROW Switch)
            switch (refund.getStatus()) {
                case PENDING -> this.statusDisplay = "대기중";
                case APPROVED -> this.statusDisplay = "승인됨";
                case REJECTED -> this.statusDisplay = "거절됨";
            }

            this.inPending = (refund.getStatus() == RefundStatus.PENDING);
            this.isApproved = (refund.getStatus() == RefundStatus.APPROVED);
            this.isRejected = (refund.getStatus() == RefundStatus.REJECTED);
        }

        public AdminListDTO(Refund refund) {
            // 우리는 한방에 User와 Payment 가지고 옴 ()
            this.id = refund.getId();
            this.username = refund.getUser().getUsername();
            this.paymentId = refund.getPayment().getId();
            this.impUid = refund.getPayment().getImpUid();
            this.merchantUid = refund.getPayment().getMerchantUid();
            this.amount = refund.getPayment().getAmount();

            this.status = refund.getStatus();
            this.reason = refund.getReason();
            this.rejectReason = refund.getRejectReason();

            // 변환 -> 대기중/승인됨/거절됨
            this.statusDisplay = statusDisplay;
            // 스위치 표현식 사용 - jdk 14버전 이상 부터 사용 가능
            switch (refund.getStatus()) {
                case PENDING -> this.statusDisplay = "대기중";
                case APPROVED -> this.statusDisplay = "승인됨";
                case REJECTED -> this.statusDisplay = "거절됨";
            }
            // 날짜 포맷
            // refundRequest.getCreatedAt() --> pc --> DB
            // 테스트 --> 샘플을 직접 insert 처리
            if (refund.getCreatedAt() != null) {
                this.requestAt
                        = MyDateUtil.timestampFormat(refund.getCreatedAt());
            }
        }
    }
}
