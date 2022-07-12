package com.icedberries.UBFunkeysServer.service.impl;

import com.icedberries.UBFunkeysServer.domain.Crib;
import com.icedberries.UBFunkeysServer.repository.CribRepository;
import com.icedberries.UBFunkeysServer.service.CribService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CribServiceImpl implements CribService {

    public final CribRepository cribRepository;

    @Override
    public Integer count() {
        return Math.toIntExact(cribRepository.count());
    }

    @Override
    public void save(Crib crib) {
        cribRepository.save(crib);
    }
}