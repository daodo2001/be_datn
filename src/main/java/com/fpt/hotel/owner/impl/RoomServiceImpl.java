package com.fpt.hotel.owner.impl;

import com.fpt.hotel.config.AppConts;
import com.fpt.hotel.model.Room;
import com.fpt.hotel.model.RoomUtility;
import com.fpt.hotel.owner.dto.response.RoomResponse;
import com.fpt.hotel.owner.service.IRoomService;
import com.fpt.hotel.repository.RoomRepository;
import com.fpt.hotel.repository.RoomUtilityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements IRoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoomUtilityRepository roomUtilityRepository;

    @Override
    public List<RoomResponse> findAll(Long idHotel, Long idTypeRoom) {
        List<Room> rooms = new ArrayList<>();
        List<RoomResponse> roomResponses = new ArrayList<>();
        List<String> images = new ArrayList<>();
        List<RoomUtility> utilityList =  new ArrayList<>();
        if (idTypeRoom != null) {
            rooms = roomRepository.findAll(idHotel, idTypeRoom);
        } else {
            rooms = roomRepository.findAll(idHotel);
        }
        for (Room room : rooms) {
            utilityList = roomUtilityRepository.findAllByIdRoom(room.getId());
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setId(room.getId());
            roomResponse.setStatus(room.getStatus());
            roomResponse.setNumberRoom(room.getNumberRoom());
            roomResponse.setNameTypeRoom(room.getTypeRoom().getName());
            roomResponse.setEnabled(room.getEnabled());
            roomResponse.setDescription(room.getDescription());
            roomResponses.add(roomResponse);
        }
        for (RoomUtility roomUtility:utilityList) {
            images.add(roomUtility.getUtility().getImage());
        }
        for (RoomResponse roomResponse:roomResponses) {
            roomResponse.setImagesTienIch(images);
        }

        return roomResponses;
    }

    @Override
    public RoomResponse save(Room room) {

        boolean check = roomRepository.existsByNumberRoom(room.getNumberRoom());
        if (check) {
            return null;
        }

        Room newRoom = roomRepository.save(room);
        return modelMapper.map(newRoom, RoomResponse.class);
    }

    @Override
    public RoomResponse updateEnabled(Long id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room.setEnabled(!room.getEnabled());
            room.setDescription(!room.getEnabled() ? AppConts.ISMAINTAINED : AppConts.STILLEMPTY);
            Room newRoom = roomRepository.save(room);
            return modelMapper.map(newRoom, RoomResponse.class);
        }
        return null;
    }
}
