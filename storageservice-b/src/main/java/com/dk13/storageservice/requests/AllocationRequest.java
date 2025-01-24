package com.dk13.storageservice.requests;

public class AllocationRequest {
    private Long allocatedSpace;
    
    public Long getAllocatedSpace() {
        return allocatedSpace;
    }
    
    public void setAllocatedSpace(Long allocatedSpace) {
        this.allocatedSpace = allocatedSpace;
    }
}
