package com.NNTDATA.TrasacctionB.business.repository;


import com.NNTDATA.TrasacctionB.Model.entity.TransaccionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITransaccionRepository extends
        ReactiveMongoRepository<TransaccionEntity, Long> {

}
