package com.fooddelivery.controller;

import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/create")
    public String createOrderForm(@RequestParam Long restaurantId, Model model) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "createOrder";
    }

    @PostMapping("/orders/create")
    public String createOrder(@RequestParam Long restaurantId,
                              @RequestParam Long[] menuItems,
                              @RequestParam String customerName,
                              @RequestParam String phoneNumber,
                              @RequestParam String address) {

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        List<MenuItem> items = Arrays.stream(menuItems)
                .map(itemId -> {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setId(itemId);
                    return menuItem;
                })
                .collect(Collectors.toList());

        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setItems(items);
        order.setCustomerName(customerName);
        order.setPhoneNumber(phoneNumber);
        order.setAddress(address);
        order.setStatus("Pending");

        orderService.createOrder(order);

        return "redirect:/orders";
    }
}
