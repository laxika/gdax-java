package com.coinbase.exchange.api.websocketfeed;

import com.coinbase.exchange.api.configuration.ApiProperties;
import com.coinbase.exchange.api.exchange.Signature;
import com.coinbase.exchange.api.websocketfeed.message.OrderBookMessage;
import com.coinbase.exchange.api.websocketfeed.message.OrderDoneOrderBookMessage;
import com.coinbase.exchange.api.websocketfeed.message.OrderMatchOrderBookMessage;
import com.coinbase.exchange.api.websocketfeed.message.Subscribe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.websocket.*;
import java.io.IOException;
import java.time.Instant;

@Slf4j
@Component
@ClientEndpoint
@RequiredArgsConstructor
public class WebsocketFeed {

    private final ApiProperties apiProperties;
    private final Signature signature;
    private final ObjectMapper objectMapper;

    Session userSession = null;
    MessageHandler messageHandler;

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason      the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for OrderBookMessage Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }


    public void subscribe(Subscribe msg) {
        String jsonSubscribeMessage = signObject(msg);

        addMessageHandler(json -> {

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                public Void doInBackground() {
                    OrderBookMessage message = getObject(json, new TypeReference<OrderBookMessage>() {
                    });

                    String type = message.getType();

                    if (type.equals("heartbeat")) {
                        log.info("heartbeat");
                    } else if (type.equals("received")) {
                        // received orders are not necessarily live orders - so I'm ignoring these msgs as they're
                        // subject to change.
                        log.info("order received {}", json);

                    } else if (type.equals("open")) {
                        log.info("Order opened: " + json);
                    } else if (type.equals("done")) {
                        log.info("Order done: " + json);
                        if (!message.getReason().equals("filled")) {
                            OrderBookMessage doneOrder = getObject(json, new TypeReference<OrderDoneOrderBookMessage>() {
                            });
                        }
                    } else if (type.equals("match")) {
                        log.info("Order matched: " + json);
                        OrderBookMessage matchedOrder = getObject(json, new TypeReference<OrderMatchOrderBookMessage>() {
                        });
                    } else if (type.equals("change")) {
                        // TODO - possibly need to provide implementation for this to work in real time.
                        log.info("Order Changed {}", json);
                        // orderBook.updateOrderBookWithChange(getObject(json, new TypeReference<OrderChangeOrderBookMessage>(){}));
                    } else {
                        // Not sure this is required unless I'm attempting to place orders
                        // ERROR
                        log.error("Error {}", json);
                        // orderBook.orderBookError(getObject(json, new TypeReference<ErrorOrderBookMessage>(){}));
                    }
                    return null;
                }

                public void done() {

                }
            };
            worker.execute();
        });

        // send message to websocket
        sendMessage(jsonSubscribeMessage);

    }

    // TODO - get this into postHandle interceptor.
    public String signObject(Subscribe jsonObj) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(jsonObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String timestamp = Instant.now().getEpochSecond() + "";
        jsonObj.setKey(apiProperties.getAccessKey());
        jsonObj.setTimestamp(timestamp);
        jsonObj.setPassphrase(apiProperties.getPassphrase());
        jsonObj.setSignature(signature.generate("", "GET", jsonString, timestamp));

        try {
            return objectMapper.writeValueAsString(jsonObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getObject(String json, TypeReference<T> type) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * OrderBookMessage handler.
     *
     * @author Jiji_Sasidharan
     */
    public interface MessageHandler {
        void handleMessage(String message);
    }
}
