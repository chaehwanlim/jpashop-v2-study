package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 * Order -> Member: ManyToOne 관계
 * Order -> Delivery: OneToOne 관계
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        /*
         * 만약 바로 return orders 한다면...?
         * 즉, 엔티티를 직접 외부에 노출한다면?
         *
         * 응답이 무한 루프에 빠짐
         * -> 양방향 연관관계로 맺어진 필드들은 다 @JsonIgnore를 걸어주어야 함
         *
         * 또한 LAZY_LOADING이면 프록시 객체를 반환할텐데
         * JacksonHibernate5 모듈 혹은 강제로 필드를 호출해 LAZY 초기화하는 방법으로 어느정도 해결이 가능하지만
         * 유지보수가 어려울 수 있음 (예를 들면 필드명을 바꿔야 할 때 번거로움)
         */
        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        /*
         * 여전히 Order 한 번 당 테이블을 2개(Member, Delivery)를 매번 조회해야 한다는 단점이 존재함
         * -> N + 1(1 + N) 문제 발생
         * 1: orders 가져오는 쿼리
         * n: 각 order와 연관된 정보(member, delivery)를 각각 가져오는 쿼리(결국 2n)
         */

        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
