package kz.muradaliev.charm.back.mapper;

public interface Mapper <From, To>{

    To map(From from);

    To map(From from, To to);
}
