package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.Order;
import edu.icet.ecom.dto.OrderDetail;
import edu.icet.ecom.entity.OrderDetailEntity;
import edu.icet.ecom.entity.OrderEntity;
import edu.icet.ecom.entity.ProductEntity;
import edu.icet.ecom.repository.OrderDetailRepository;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper mapper;

    @Transactional

    @Override
    public void placeOrder(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(order.getCustomerName());
        orderEntity.setTotalAmount(order.getTotalAmount());
        orderEntity.setReceivedAmount(order.getReceivedAmount());

        OrderEntity saveOrder = orderRepository.save(orderEntity);

        List<OrderDetailEntity> orderDetailEntities = order.getOrderDetails().stream().map(orderDetail -> {
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            orderDetailEntity.setOrder(saveOrder);
            orderDetailEntity.setProduct(mapper.map(orderDetail.getProduct(), ProductEntity.class));
            orderDetailEntity.setQty(orderDetail.getQty());
            orderDetailEntity.setTotal(orderDetail.getTotal());
            return orderDetailEntity;
        }).collect(Collectors.toList());

        orderDetailRepository.saveAll(orderDetailEntities);
        saveOrder.setOrderDetails(orderDetailEntities);

    }

    @Override
    public List<Order> getAll() {
        List<OrderEntity> all = orderRepository.findAll();

        List<Order> orders = new ArrayList<>();

        all.forEach(orderEntity -> {
            orders.add(mapper.map(orderEntity, Order.class));
        });
        return orders;
    }

}
