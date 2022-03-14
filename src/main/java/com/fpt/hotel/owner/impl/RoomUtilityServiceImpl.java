package com.fpt.hotel.owner.impl;

import com.fpt.hotel.model.Room;
import com.fpt.hotel.model.RoomUtility;
import com.fpt.hotel.owner.dto.request.TypeRoomUtilityRequest;
import com.fpt.hotel.owner.dto.response.RoomUtilityResponse;
import com.fpt.hotel.owner.service.RoomUtilityService;
import com.fpt.hotel.repository.RoomRepository;
import com.fpt.hotel.repository.RoomUtilityRepository;
import com.fpt.hotel.repository.UtitlityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomUtilityServiceImpl implements RoomUtilityService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomUtilityRepository roomUtilityRepository;

    @Autowired
    UtitlityRepository utitlityRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<RoomUtilityResponse> saveTypeRoomUtitiliy(TypeRoomUtilityRequest typeRoomUtilityRequest) {
        Long idHotel = typeRoomUtilityRequest.getIdHotel();
        Long idTypeRoom = typeRoomUtilityRequest.getIdTypeRoom();
        Long idUtility = typeRoomUtilityRequest.getIdTienIch();
        List<Room> findAll = roomRepository.findAll(idHotel, idTypeRoom);
        List<RoomUtility> utilityList = new ArrayList<>();
        for (Room room : findAll) {
            Optional<RoomUtility> roomUtilityOptional =
                    roomUtilityRepository.findByIdRoomAndIdUtility(room.getId(), idUtility);
            if (roomUtilityOptional.isEmpty()) {
                RoomUtility roomUtility = new RoomUtility();
                roomUtility.setUtility(utitlityRepository.findById(typeRoomUtilityRequest.getIdTienIch()).get());
                roomUtility.setRoom(room);
                roomUtility.setQuantity(1);
                roomUtilityRepository.save(roomUtility);
                utilityList.add(roomUtility);
            }
        }
        return utilityList.stream().map(roomUtility ->
                modelMapper.map(roomUtility, RoomUtilityResponse.class)).collect(Collectors.toList());
    }
}
