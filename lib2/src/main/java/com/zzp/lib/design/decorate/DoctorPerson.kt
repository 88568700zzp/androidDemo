package com.zzp.lib.design.decorate

class DoctorPerson : Person() {
    override fun getMessage(): String {
        return super.getMessage() + " and DoctorPerson"
    }
}