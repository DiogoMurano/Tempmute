package br.com.diogomurano.tempmute.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MutePlayer {

    private String playerName;
    private long expires;

    public MutePlayer with(Consumer<MutePlayer> consumer) {
        consumer.accept(this);
        return this;
    }

    public boolean isMuted() {
        return expires > System.currentTimeMillis();
    }

}
