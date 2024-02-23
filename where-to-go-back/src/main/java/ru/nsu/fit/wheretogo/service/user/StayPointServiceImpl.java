package ru.nsu.fit.wheretogo.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.user.StayPointDTO;
import ru.nsu.fit.wheretogo.entity.place.StayPoint;
import ru.nsu.fit.wheretogo.repository.user.StayPointRepository;
import ru.nsu.fit.wheretogo.repository.user.UserRepository;
import ru.nsu.fit.wheretogo.utils.helpers.SecurityContextHelper;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class StayPointServiceImpl implements StayPointService {

    private final StayPointRepository stayPointRepository;
    private final UserRepository userRepository;

    public StayPointServiceImpl(
            StayPointRepository stayPointRepository,
            UserRepository userRepository) {
        this.stayPointRepository = stayPointRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addStoryPoint(double lat, double lon) {

        var stayPoint = new StayPoint()
                .setUser(userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow())
                .setLatitude(lat)
                .setLongitude(lon);

        List<StayPoint> userStayPoints = userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow().getStayPoints();
        var exist = false;
        var eps = 0.000001;

        for (StayPoint userStayPoint : userStayPoints) {
            if (Math.abs(userStayPoint.getLatitude() - lat) < eps
                    && Math.abs(userStayPoint.getLongitude() - lon) < eps) {
                exist = true;
                break;
            }
        }

        if (exist) {
            userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                    currentUser -> currentUser.getStayPoints().removeIf(
                            currStayPoint -> currStayPoint.getLatitude() == lat && currStayPoint.getLongitude() == lon)
            );
            userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                    currentUser -> currentUser.getStayPoints().add(stayPoint)
            );
            stayPointRepository.setNewPointTimeStamp(Instant.now());
        } else {
            userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                    currentUser -> currentUser.getStayPoints().add(stayPoint)
            );
            stayPointRepository.save(stayPoint);
        }

        if (stayPointRepository.countByUserId(
                userRepository.findByEmail(SecurityContextHelper.email())
                        .orElseThrow()
                        .getId()) > 5) {
            deleteOldStoryPoint();
        }
    }

    @Override
    @Transactional
    public void deleteOldStoryPoint() {
        List<StayPoint> userStayPoints = userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow().getStayPoints();
        userStayPoints.sort(Comparator.comparing(StayPoint::getUploadedAt));
        Instant oldTimeStamp = userStayPoints.get(0).getUploadedAt();

        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getStayPoints().removeIf(
                        storyPoint -> Objects.equals(storyPoint.getUploadedAt(), oldTimeStamp)));

        stayPointRepository.deleteByUploadedAt(oldTimeStamp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StayPointDTO> getByUser() {
        return userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints().stream().map(
                StayPointDTO::getFromEntity
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean ifUserHasStayPoints() {
        return stayPointRepository.existsByUserId(userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow().getId());
    }

}
