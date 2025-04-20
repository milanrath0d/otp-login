/**
 * JavaScript for OTP Verification
 */

$(document).ready(function() {
    // Get username from localStorage
    const username = localStorage.getItem('otpUsername');
    const otpType = localStorage.getItem('otpType') || 'phone';
    const destination = localStorage.getItem('otpDestination') || '';
    
    // Set username in hidden field
    $('#hiddenUsername').val(username);
    
    // Add destination info to the page
    if (destination) {
        let infoText = '';
        if (otpType === 'phone') {
            // Mask the phone number for privacy
            const maskedPhone = maskPhoneNumber(destination);
            infoText = `Please enter the OTP sent to ${maskedPhone}`;
        } else {
            // Mask the email for privacy
            const maskedEmail = maskEmail(destination);
            infoText = `Please enter the OTP sent to ${maskedEmail}`;
        }
        
        // Insert the message if not already present
        if ($('.otp-destination-info').length === 0) {
            $('<p>')
                .addClass('text-center mb-3 otp-destination-info')
                .text(infoText)
                .insertAfter('.card-body p:first');
        }
    }
    
    // OTP verification form submission
    $('#verifyForm').on('submit', function(e) {
        e.preventDefault();
        
        // Hide any existing alerts
        $('.alert').addClass('d-none');
        
        const otp = $('#otp').val();
        const username = $('#hiddenUsername').val();
        
        // Validate OTP format
        if (!otp.match(/^\d{6}$/)) {
            $('#errorAlert').text('Please enter a 6-digit OTP').removeClass('d-none');
            return;
        }
        
        // Send AJAX request to validate OTP
        $.ajax({
            url: '/router/validateOTP',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userName: username,
                oneTimePassword: otp
            }),
            success: function(response) {
                // Show success message
                $('#successAlert').removeClass('d-none');
                
                // Clear localStorage
                localStorage.removeItem('otpUsername');
                localStorage.removeItem('otpType');
                localStorage.removeItem('otpDestination');
                
                // Redirect to home or protected page
                setTimeout(function() {
                    window.location.href = '/';
                }, 2000);
            },
            error: function(xhr) {
                // Show error message
                $('#errorAlert').text(xhr.responseJSON || 'Invalid OTP. Please try again.').removeClass('d-none');
            }
        });
    });
    
    // Resend OTP button click
    $('#resendOtpBtn').on('click', function() {
        // Hide any existing alerts
        $('.alert').addClass('d-none');
        
        const username = $('#hiddenUsername').val();
        const destination = localStorage.getItem('otpDestination');
        
        if (!username || !destination) {
            $('#errorAlert').text('Missing information. Please return to the previous page.').removeClass('d-none');
            return;
        }
        
        // Send AJAX request to resend OTP
        $.ajax({
            url: '/router/sendOTP',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userName: username,
                phoneNumber: destination
            }),
            success: function(response) {
                // Show temporary success message
                $('<div>')
                    .addClass('alert alert-info')
                    .text('OTP has been resent')
                    .insertAfter('#verifyForm')
                    .delay(3000)
                    .fadeOut(function() {
                        $(this).remove();
                    });
            },
            error: function(xhr) {
                // Show error message
                $('#errorAlert').text('Failed to resend OTP: ' + (xhr.responseJSON?.message || 'Unknown error')).removeClass('d-none');
            }
        });
    });
    
    // Helper function to mask phone number
    function maskPhoneNumber(phone) {
        if (!phone) return '';
        
        // Keep first two and last two digits visible
        const visibleStart = phone.slice(0, 2);
        const visibleEnd = phone.slice(-2);
        const masked = '*'.repeat(Math.max(0, phone.length - 4));
        
        return visibleStart + masked + visibleEnd;
    }
    
    // Helper function to mask email
    function maskEmail(email) {
        if (!email || !email.includes('@')) return '';
        
        const parts = email.split('@');
        const name = parts[0];
        const domain = parts[1];
        
        // Keep first two and last character of the name visible
        const visibleStart = name.slice(0, 2);
        const visibleEnd = name.slice(-1);
        const maskedName = visibleStart + '*'.repeat(Math.max(0, name.length - 3)) + visibleEnd;
        
        return maskedName + '@' + domain;
    }
}); 