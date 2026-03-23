## 개요
- 상품(Product)과 상품 출시 단계(ProductStatus) 도메인의 기본 구조를 정비하고, 상품 출시 단계 변경 요청에 대해 공통 결재 모듈을 통해 승인/반려 및 후처리까지 연결되는 흐름을 구현했습니다.
- 기존 상품 도메인 내부에 흩어져 있던 결재 관련 요소를 `global/approval` 패키지로 분리하여 공통 결재 기능으로 재구성했고, 현재는 `PRODUCT_STATUS` 도메인에 대해 최종 승인 시 실제 상품 출시 단계가 반영되도록 구현했습니다. :contentReference[oaicite:0]{index=0}

## 주요 변경사항

### 1. 공통 결재 모듈 신설 및 상품 상태 변경 결재 흐름 연결
- 변경 목적
    - 상품 출시 단계 변경 요청을 상품 도메인 내부의 개별 승인 구조가 아니라, 공통 결재 문서/결재선 기반으로 처리할 수 있도록 구조를 정리했습니다.
    - 결재 승인/반려 처리와 도메인 후처리를 분리해, 향후 원가(COST), 가격(PRICING) 등 다른 결재 대상 도메인으로 확장 가능한 기반을 마련했습니다.
- 해당 파일명
    - `src/main/java/com/bapful/fabricore/global/approval/controller/ApprovalController.java`
    - `src/main/java/com/bapful/fabricore/global/approval/dto/ApprovalActionRequest.java`
    - `src/main/java/com/bapful/fabricore/global/approval/dto/ApprovalCreateCommand.java`
    - `src/main/java/com/bapful/fabricore/global/approval/dto/ApprovalResult.java`
    - `src/main/java/com/bapful/fabricore/global/approval/entity/ApprovalDocument.java`
    - `src/main/java/com/bapful/fabricore/global/approval/entity/ApprovalLine.java`
    - `src/main/java/com/bapful/fabricore/global/approval/enums/ApprovalDomainType.java`
    - `src/main/java/com/bapful/fabricore/global/approval/enums/ApprovalStatus.java`
    - `src/main/java/com/bapful/fabricore/global/approval/repository/ApprovalDocumentRepository.java`
    - `src/main/java/com/bapful/fabricore/global/approval/repository/ApprovalLineRepository.java`
    - `src/main/java/com/bapful/fabricore/global/approval/service/ApprovalService.java`
    - `src/main/java/com/bapful/fabricore/global/approval/service/ApprovalWorkflowService.java`

### 2. 상품 출시 단계 변경 요청/반영 기능 분리
- 변경 목적
    - 상품 출시 단계 변경 요청 생성과, 결재 완료 후 실제 상품 상태를 반영하는 책임을 `ProductStatusService`로 분리했습니다.
    - 결재 요청 시 `ProductStatus` 이력을 생성하고, 결재 문서 ID를 매핑한 뒤, 최종 승인 시 `Product.launchStatus`를 변경하도록 구성했습니다.
- 해당 파일명
    - `src/main/java/com/bapful/fabricore/product/controller/ProductStatusController.java`
    - `src/main/java/com/bapful/fabricore/product/dto/request/StatusChangeRequest.java`
    - `src/main/java/com/bapful/fabricore/product/dto/response/StatusChangeResponse.java`
    - `src/main/java/com/bapful/fabricore/product/entity/ProductStatus.java`
    - `src/main/java/com/bapful/fabricore/product/repository/ProductStatusRepository.java`
    - `src/main/java/com/bapful/fabricore/product/service/ProductStatusService.java`

### 3. 상품 도메인 구조 보강 및 수동 상태 변경 API 정리
- 변경 목적
    - 상품 생성 시 필요한 속성(시즌 코드, 색상 코드 등)을 보강하고, 상품 코드 생성 규칙에 색상 코드를 포함하도록 수정했습니다.
    - 상품 목록/상세 조회 및 수동 출시 단계 변경 API를 정리해 기본 상품 관리 기능을 확장했습니다.
- 해당 파일명
    - `src/main/java/com/bapful/fabricore/product/controller/ProductController.java`
    - `src/main/java/com/bapful/fabricore/product/dto/request/ProductCreateRequest.java`
    - `src/main/java/com/bapful/fabricore/product/entity/Product.java`
    - `src/main/java/com/bapful/fabricore/product/entity/ProductCodeSeq.java`
    - `src/main/java/com/bapful/fabricore/product/enums/LaunchStatus.java`
    - `src/main/java/com/bapful/fabricore/product/repository/ProductCodeSeqRepository.java`
    - `src/main/java/com/bapful/fabricore/product/service/ProductService.java`

### 4. 기존 상품 전용 승인 구조 제거 및 공통 결재 구조로 통합
- 변경 목적
    - 기존 `ProductStatusApproval`, `ProductStatusApprovalRepository`, 상품 도메인 하위 `ApprovalStatus` 등 상품 전용 승인 구조를 제거/이관해 중복 개념을 줄였습니다.
    - 결재 상태 enum과 결재 엔티티를 공통 패키지로 이동시켜 유지보수성과 재사용성을 높였습니다.
- 해당 파일명
    - 삭제: `src/main/java/com/bapful/fabricore/product/entity/ProductStatusApproval.java`
    - 삭제: `src/main/java/com/bapful/fabricore/product/repository/ProductStatusApprovalRepository.java`
    - 이동/변경: `src/main/java/com/bapful/fabricore/product/enums/ApprovalStatus.java`
    - 생성: `src/main/java/com/bapful/fabricore/global/approval/entity/ApprovalLine.java`
    - 생성: `src/main/java/com/bapful/fabricore/global/approval/enums/ApprovalStatus.java`

### 5. 감사 로그 및 환경 설정 정비
- 변경 목적
    - 공통 감사 엔티티의 setter 허용 등 수정 가능성을 보완해 서비스 로직에서 감사 필드를 활용할 수 있도록 조정했습니다.
    - 로컬 프로필 활성화와 환경별 yml ignore 규칙을 추가해 로컬 개발 환경 분리를 준비했습니다.
- 해당 파일명
    - `src/main/java/com/bapful/fabricore/global/common/audit/BaseAudit.java`
    - `.gitignore`
    - `src/main/resources/application.yml`

### 6. 빌드 의존성 및 로컬 실행 기반 정리
- 변경 목적
    - Swagger UI, JPA, Validation, MySQL 드라이버 등 현재 개발 단계에 필요한 실행 의존성을 유지하고, 보안/테스트 의존성 일부를 조정해 로컬 개발 환경을 단순화했습니다.
- 해당 파일명
    - `build.gradle`

## 참고(비고)
- 현재 공통 결재 후처리는 `ApprovalDomainType.PRODUCT_STATUS`에 대해서만 연결되어 있으며, `COST`, `PRICING`은 enum만 정의된 상태입니다. 후속 PR에서 도메인별 후처리 구현이 필요합니다. :contentReference[oaicite:1]{index=1}
- `ApprovalWorkflowService`에는 아직 미사용 의존성 및 `switch` 기반 분기 구조가 남아 있어, 도메인 확장 시 handler 분리 여부를 추가 검토할 예정입니다. :contentReference[oaicite:2]{index=2}
- `ProductStatusRepository`의 `findByProductIdOrderByRequestedAtDesc`는 현재 `ProductStatus` 필드 구조와 불일치 가능성이 있어 정합성 점검이 필요합니다. `ProductStatus`는 `requestedAt` 대신 `statusChangedAt`, `approvalDocumentId` 중심으로 변경되었습니다. :contentReference[oaicite:3]{index=3}
- `src/main/resources/application.yaml`은 `application.yml`로 변경되었고, active profile이 `local`로 설정되었습니다. 로컬 실행 시 별도 `application-local.yaml` 준비가 필요합니다. :contentReference[oaicite:4]{index=4}