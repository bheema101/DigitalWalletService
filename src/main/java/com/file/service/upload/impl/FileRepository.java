package com.file.service.upload.impl;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.file.model.Fileinfo;

@EnableScan
public interface  FileRepository extends CrudRepository<Fileinfo, String> {

}
