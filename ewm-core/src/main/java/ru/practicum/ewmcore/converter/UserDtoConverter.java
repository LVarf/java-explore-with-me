package ru.practicum.ewmcore.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.service.utils.SortConverterMixin;

public class UserDtoConverter implements RootModelConverter<UserFullDto, User>,
        SortConverterMixin {
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

    @Override
    public Sort.Order convertSortPropertyToEntityStyle(Sort.Order order) {
        final String property = order.getProperty();
        return order;
    }

    @Override
    public Pageable secondarySort(Pageable pageable, String fieldName) {
        return null /*PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sortUtils.addSecondarySorting(pageable.getSort(),
                        fieldName))*/;
    }
}
