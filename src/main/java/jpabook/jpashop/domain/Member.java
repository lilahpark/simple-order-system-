package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    //엔티티의 이름이 변경되면 NotEmpty로 인해 api 스펙 자체가 작동하지 않게 되는 위험이 있다
    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    //persist () 후
    //컬렉션 생성후 밖으로 꺼내지 말고 orders 컬렉션 자체를 변경해선 안된다.
    //안정성을 위해.이것은 이제 하이버네이트가 관리하는 커넥션이기 때문이다
    //변경하면 하이버네이트가 동작하는 매커니즘 대로 동작하지 않을 수 있다.
}
