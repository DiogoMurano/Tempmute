package br.com.diogomurano.tempmute.services;

import br.com.diogomurano.tempmute.model.MutePlayer;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

public interface MutePlayerService {

    ImmutableList<MutePlayer> getAll();

    Optional<MutePlayer> findByName(String playerName);

    void add(MutePlayer player);

    void remove(MutePlayer player);

}
