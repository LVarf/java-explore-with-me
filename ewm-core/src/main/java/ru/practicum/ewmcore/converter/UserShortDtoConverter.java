package ru.practicum.ewmcore.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserShortDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
public class UserShortDtoConverter implements RootModelConverter<UserShortDto, User> {

    @Override
    public UserShortDto convertFromEntity(final User entity) {
        final UserShortDto model = new UserShortDto();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public User convertToEntity(final UserShortDto entity) {
        final User model = new User();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public User mergeToEntity(final UserShortDto model, final User entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

}
