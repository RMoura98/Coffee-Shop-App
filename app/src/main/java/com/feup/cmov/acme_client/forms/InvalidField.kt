package com.feup.cmov.acme_client.forms

// This class represents an error related to a field in a form.
// E.g. The user chooses a expiration date for the credit card which is invalid.
data class InvalidField (val fieldName: String, val msg: String)