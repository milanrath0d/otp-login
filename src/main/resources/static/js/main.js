/**
 * Main JavaScript for OTP Authentication UI
 */

$(document).ready(function() {
    // Phone OTP form submission
    $('#phoneForm').on('submit', function(e) {
        e.preventDefault();
        const username = $('#username').val();
        const phoneNumber = $('#phoneNumber').val();
        
        // Hide any existing alerts
        $('.alert').addClass('d-none');
        
        // Send AJAX request to generate SMS OTP
        $.ajax({
            url: '/router/sendOTP',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userName: username,
                phoneNumber: phoneNumber
            }),
            success: function(response) {
                // Show success message
                $('#successAlert').removeClass('d-none');
                
                // Store username in localStorage for verification page
                localStorage.setItem('otpUsername', username);
                localStorage.setItem('otpType', 'phone');
                localStorage.setItem('otpDestination', phoneNumber);
                
                // Redirect to verification page after a short delay
                setTimeout(function() {
                    window.location.href = '/verify';
                }, 1500);
            },
            error: function(xhr) {
                // Show error message
                $('#errorAlert').text('Failed to send OTP: ' + (xhr.responseJSON?.message || 'Unknown error')).removeClass('d-none');
            }
        });
    });
    
    // Email OTP form submission
    $('#emailForm').on('submit', function(e) {
        e.preventDefault();
        const username = $('#emailUsername').val();
        const email = $('#emailAddress').val().trim();
        
        // Debug: log the values
        console.log('Username:', username);
        console.log('Email:', email);
        
        // Hide any existing alerts
        $('.alert').addClass('d-none');
        
        // Create the request payload
        const payload = {
            userName: username,
            email: email
        };
        
        // Debug: log the payload
        console.log('Sending payload:', payload);
        
        // Send AJAX request to generate Email OTP
        $.ajax({
            url: '/router/sendOTP',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(response) {
                console.log('Success response:', response);
                // Show success message
                $('#successAlert').removeClass('d-none');
                
                // Store username in localStorage for verification page
                localStorage.setItem('otpUsername', username);
                localStorage.setItem('otpType', 'email');
                localStorage.setItem('otpDestination', email);
                
                // Redirect to verification page after a short delay
                setTimeout(function() {
                    window.location.href = '/verify';
                }, 1500);
            },
            error: function(xhr) {
                console.error('Error response:', xhr);
                // Show error message
                $('#errorAlert').text('Failed to send OTP: ' + (xhr.responseJSON?.message || 'Unknown error')).removeClass('d-none');
            }
        });
    });
}); 