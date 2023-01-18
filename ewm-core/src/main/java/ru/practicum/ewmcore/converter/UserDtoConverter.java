package ru.practicum.ewmcore.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
public class UserDtoConverter implements RootModelConverter<UserFullDto, User> {
    @Override
    public UserFullDto convertFromEntity(final User entity) {
        final UserFullDto model = new UserFullDto();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public User convertToEntity(final UserFullDto entity) {
        final User model = new User();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public User mergeToEntity(final UserFullDto model, final User entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
