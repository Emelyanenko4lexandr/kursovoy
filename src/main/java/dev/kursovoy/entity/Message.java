package dev.kursovoy.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "messages")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User sender;

    @OneToOne
    private User recipient;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String message;

    private String reason;

    public Message(Long id, User sender, User owner, MessageType messageType, String messageText) {
        this.id = id;
        this.sender = sender;
        this.recipient = owner;
        this.type = messageType;
        this.message = messageText;
    }
}
