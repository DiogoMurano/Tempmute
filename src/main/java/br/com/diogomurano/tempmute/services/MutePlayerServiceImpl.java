package br.com.diogomurano.tempmute.services;

import br.com.diogomurano.tempmute.model.MutePlayer;
import com.google.common.collect.ImmutableList;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class MutePlayerServiceImpl implements MutePlayerService {

    private Set<MutePlayer> collection;

    public MutePlayerServiceImpl() {
        collection = new HashSet<>();
    }

    @Override
    public ImmutableList<MutePlayer> getAll() {
        return ImmutableList.copyOf(collection);
    }

    @Override
    public Optional<MutePlayer> findByName(String playerName) {
        return collection.stream().filter(player -> player.getPlayerName().equals(playerName)).findAny();
    }

    @Override
    public void add(MutePlayer player) {
        Objects.requireNonNull(player, "player can't be null.");
        this.collection.add(player);
    }

    @Override
    public void remove(MutePlayer player) {
        Objects.requireNonNull(player, "player can't be null.");
        this.collection.remove(player);
    }
}
