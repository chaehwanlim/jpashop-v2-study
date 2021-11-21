package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
