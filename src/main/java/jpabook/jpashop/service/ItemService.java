package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);

    }

    //머지가 아닌 변경감지를 사용해야 한다( 머지는 모든 데이터를 걍 업데이트 해버림(지정하지 않은 set 컬럼을 null로 입력 되기도)
    //트랜잭션 안에서 엔티티를 조회해야 영속성 상태로 조회되고
    //거기에서 값을 변경해서 더티체킹(변경감지)가 일어난다
    //트랜잭션이 커밋될 때 plush가 일어나면서 변경감지가 되면서 update -> db 반영
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


}
