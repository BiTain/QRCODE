package com.bitain.qrcode.service.imp;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bitain.qrcode.dto.BaseResponse;
import com.bitain.qrcode.entity.AttendanceEntity;
import com.bitain.qrcode.repository.AttendanceRepository;
import com.bitain.qrcode.utils.QrCodeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitain.qrcode.dto.user.UserDto;
import com.bitain.qrcode.dto.user.UserRequest;
import com.bitain.qrcode.entity.UserEntity;
import com.bitain.qrcode.exception.ComponentException;
import com.bitain.qrcode.exception.ResourceConflictException;
import com.bitain.qrcode.exception.ResourceNotFoundException;
import com.bitain.qrcode.repository.UserRepository;
import com.bitain.qrcode.service.IUserService;

@Service
public class UserService extends AbstractService<UserDto,UserEntity> implements IUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private QrCodeUtil qrCodeUtil;
    private AttendanceRepository attendanceRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper,
                       QrCodeUtil qrCodeUtil,
                       AttendanceRepository attendanceRepository){
        super(userRepository, UserDto.class, UserEntity.class);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.qrCodeUtil = qrCodeUtil;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(List<Long> idusers) {
        try{
            idusers.forEach(id->
            {
                UserEntity userEntity = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","id",String.valueOf(id)));
                userRepository.deleteById(userEntity.getId());
            });
        }catch (Exception e){
            throw new ComponentException(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse attendance(String qrCode, Long idUser) {
        UserEntity userEntity = userRepository.findById(idUser).orElseThrow(()->new ResourceNotFoundException("User","id",String.valueOf(idUser)));
        String attendanceType = qrCodeUtil.validateQrCode(qrCode);
        AttendanceEntity attendanceEntity = statusDisable(userEntity);
        switch (attendanceType){
            case "checkin":{
                if (attendanceEntity.getTimeIn()!=null){
                    throw new ComponentException("You checked",HttpStatus.BAD_REQUEST);
                }
                attendanceEntity.setUser(userEntity);
                attendanceEntity.setDate(new Date());
                attendanceEntity.setTimeIn(LocalTime.now());
                attendanceRepository.save(attendanceEntity);
                return new BaseResponse(200,"Chek-In success");
            }
            case "checkout":{
                if(attendanceEntity.getTimeIn()==null){
                    throw new ComponentException("You don't check-in",HttpStatus.BAD_REQUEST);
                }
                attendanceEntity.setTimeOut(LocalTime.now());
                attendanceEntity.setStatus(true);
                attendanceRepository.save(attendanceEntity);
                return  new BaseResponse(200,"Check-Out success");
            }
            default: return null;
        }
    }

    private AttendanceEntity statusDisable(UserEntity user){
        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByDateAndUser(new Date(),user);
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        if (attendanceEntities.isEmpty() ){
            return attendanceEntity;
        }
        for (AttendanceEntity item : attendanceEntities){
            if (!item.getStatus()){
                attendanceEntity = item;
                break;
            }
        }
        return attendanceEntity;
    }

    private boolean isEmailUniqe(Long userId,String email){
        Optional<UserEntity> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()){
            return true;
        }
        if(userId==null){
            return false;
        }else{
            if(userByEmail.get().getId() != userId){
                return false;
            }
        }
        return true;
    }

    @Override
    public UserEntity transformDtoToEntity(UserDto dto) {
        UserRequest userRequest = (UserRequest) dto;
        Long userId = userRequest.getId();
        String email = userRequest.getEmail();
        if (!isEmailUniqe(userId,email)) {
            throw new ResourceConflictException(email, "Email already exists");
        }
        if(userId == null){
            UserEntity userEntity = modelMapper.map(userRequest,UserEntity.class);
            String hashPassword = passwordEncoder.encode(userRequest.getPassWord());
            userEntity.setPassWord(hashPassword);
            return  userEntity;
        }else{
            UserEntity userInDb = userRepository.findById(userId).get();
            modelMapper.map(userRequest,userInDb);
            if (userRequest.getPassWord()!=null){
                String hashPassword = passwordEncoder.encode(userRequest.getPassWord());
                userInDb.setPassWord(hashPassword);
            }
            return userInDb;
        }
    }
}
