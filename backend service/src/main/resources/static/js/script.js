$(function (){
    // Function to update validation summary
    function updateValidationSummary(validator) {
        var errorList = $('#error-list');
        var validationSummary = $('#validation-summary');
        
        if(validator && validator.numberOfInvalids() > 0) {
            errorList.empty();
            var errors = validator.errorList;
            
            $.each(errors, function(index, error) {
                var fieldLabel = $('label[for="' + error.element.id + '"]').first().text() || 
                                $(error.element).attr('name') || 
                                'Field';
                fieldLabel = fieldLabel.replace('*', '').trim();
                
                errorList.append('<li>' + fieldLabel + ': ' + error.message + '</li>');
            });
            
            validationSummary.slideDown(300);
            
            // Scroll to validation summary
            $('html, body').animate({
                scrollTop: validationSummary.offset().top - 100
            }, 500);
        } else {
            validationSummary.slideUp(300);
        }
    }
    
    // Common validation settings
    $.validator.setDefaults({
        errorElement: 'label',
        errorClass: 'error',
        validClass: 'valid',
        errorPlacement: function(error, element) {
            error.insertAfter(element);
        },
        highlight: function(element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);
            $(element).closest('.form-group').addClass('has-error').removeClass('has-success');
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass);
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
        },
        success: function(label, element) {
            $(element).removeClass('error').addClass('valid');
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            label.remove();
        },
        invalidHandler: function(event, validator) {
            updateValidationSummary(validator);
        }
    });

    // User Registration Form Validation
    var $userRegister=$("#userRegister");

    $userRegister.validate({
        rules:{
            name:{
               required: true,
               lettersonly: true,
               minlength: 2,
               maxlength: 50
            },
            email:{
                required: true,
                space: true,
                email: true
            },
            mobileNumber:{
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            password:{
                required: true,
                space: true,
                minlength: 6,
                maxlength: 20
            },
            confirmpassword:{
                required: true,
                space: true,
                equalTo: '#pass'
            },
            address:{
                required: true,
                all: true,
                minlength: 5
            },
            city:{
                required: true,
                lettersonly: true
            },
            state:{
                required: true,
                lettersonly: true
            },
            pincode:{
                required: true,
                space: true,
                numericOnly: true,
                minlength: 5,
                maxlength: 6
            },
            img:{
                required: true,
            }
        },
        messages:{
            name: {
                required: "Please enter your full name",
                lettersonly: "Name can only contain letters",
                minlength: "Name must be at least 2 characters",
                maxlength: "Name cannot exceed 50 characters"
            },
            mobileNumber:{
                required: 'Please enter your mobile number',
                numericOnly: "Mobile number can only contain digits",
                space: "Spaces are not allowed",
                minlength: "Mobile number must be at least 10 digits",
                maxlength: "Mobile number cannot exceed 12 digits"
            },
            email:{
                required: 'Please enter your email address',
                space: 'Spaces are not allowed',
                email: 'Please enter a valid email address'
            },
            password:{
                required: 'Please enter a password',
                space: "Spaces are not allowed",
                minlength: "Password must be at least 6 characters",
                maxlength: "Password cannot exceed 20 characters"
            },
            confirmpassword:{
                required: 'Please confirm your password',
                space: "Spaces are not allowed",
                equalTo: 'Passwords do not match'
            },
            address:{
                required: 'Please enter your address',
                all: "Please enter a valid address",
                minlength: "Address must be at least 5 characters"
            },
            city:{
                required: 'Please enter your city',
                lettersonly: "City name can only contain letters"
            },
            state:{
                required: 'Please enter your state',
                lettersonly: "State name can only contain letters"
            },
            pincode:{
                required: 'Please enter your pincode',
                space: "Spaces are not allowed",
                numericOnly: "Pincode can only contain digits",
                minlength: "Pincode must be at least 5 digits",
                maxlength: "Pincode cannot exceed 6 digits"
            },
            img:{
                required: 'Please upload a profile image',
            }
        },
        submitHandler: function(form) {
            // Show loading state
            var submitBtn = $(form).find('button[type="submit"]');
            var originalText = submitBtn.html();
            submitBtn.html('<i class="fa-solid fa-spinner fa-spin me-2"></i>Creating Account...').prop('disabled', true);
            form.submit();
        }
    })

    //Orders Validation
    var $orders=$("#orders");
    $orders.validate({
        rules:{
            firstName:{
                required: true,
                lettersonly: true,
                minlength: 2,
                maxlength: 30
            },
            lastName:{
                required: true,
                lettersonly: true,
                minlength: 2,
                maxlength: 30
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNo: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            address: {
                required: true,
                all: true,
                minlength: 5
            },
            city: {
                required: true,
                lettersonly: true
            },
            state: {
                required: true,
                lettersonly: true
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 5,
                maxlength: 6
            },
            paymentType:{
                required: true
            }
        },
        messages:{
            firstName:{
                required: 'Please enter your first name',
                lettersonly: 'First name can only contain letters',
                minlength: 'First name must be at least 2 characters',
                maxlength: 'First name cannot exceed 30 characters'
            },
            lastName:{
                required: 'Please enter your last name',
                lettersonly: 'Last name can only contain letters',
                minlength: 'Last name must be at least 2 characters',
                maxlength: 'Last name cannot exceed 30 characters'
            },
            email: {
                required: 'Please enter your email address',
                space: 'Spaces are not allowed',
                email: 'Please enter a valid email address'
            },
            mobileNo: {
                required: 'Please enter your mobile number',
                space: 'Spaces are not allowed',
                numericOnly: 'Mobile number can only contain digits',
                minlength: 'Mobile number must be at least 10 digits',
                maxlength: 'Mobile number cannot exceed 12 digits'
            },
            address: {
                required: 'Please enter your delivery address',
                all: 'Please enter a valid address',
                minlength: 'Address must be at least 5 characters'
            },
            city: {
                required: 'Please enter your city',
                lettersonly: 'City name can only contain letters'
            },
            state: {
                required: 'Please enter your state',
                lettersonly: 'State name can only contain letters'
            },
            pincode: {
                required: 'Please enter your pincode',
                space: 'Spaces are not allowed',
                numericOnly: 'Pincode can only contain digits',
                minlength: 'Pincode must be at least 5 digits',
                maxlength: 'Pincode cannot exceed 6 digits'
            },
            paymentType:{
                required: 'Please select a payment method'
            }
        },
        submitHandler: function(form) {
            // Show loading state
            var submitBtn = $(form).find('button[type="submit"]');
            var originalText = submitBtn.html();
            submitBtn.html('<i class="fa-solid fa-spinner fa-spin me-2"></i>Processing Order...').prop('disabled', true);
            form.submit();
        }
    })

    // Real-time form feedback
    $('input, select, textarea').on('blur', function() {
        if($(this).valid()) {
            $(this).addClass('valid').removeClass('error');
        }
    });

    // Password strength indicator
    $('#pass').on('keyup', function() {
        var password = $(this).val();
        var strength = 0;
        
        if(password.length >= 6) strength++;
        if(password.length >= 10) strength++;
        if(/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
        if(/[0-9]/.test(password)) strength++;
        if(/[^a-zA-Z0-9]/.test(password)) strength++;
        
        var strengthText = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
        var strengthColor = ['#dc2626', '#f59e0b', '#fbbf24', '#16a34a', '#059669'];
        
        if(password.length > 0) {
            var indicator = $('#password-strength');
            if(indicator.length === 0) {
                $(this).after('<div id="password-strength" style="margin-top: 6px; font-size: 12px; font-weight: 500;"></div>');
                indicator = $('#password-strength');
            }
            indicator.text('Password Strength: ' + strengthText[strength])
                     .css('color', strengthColor[strength]);
        } else {
            $('#password-strength').remove();
        }
    });

})


jQuery.validator.addMethod('lettersonly',function (value,element){
    return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
});
    jQuery.validator.addMethod('space', function(value, element) {
        return /^[^-\s]+$/.test(value);
    });

    jQuery.validator.addMethod('all', function(value, element) {
        return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
    });


    jQuery.validator.addMethod('numericOnly', function(value, element) {
        return /^[0-9]+$/.test(value);
    });