// ==================== CONFIGURATION ====================
// IMPORTANT: Replace these with your actual PayHere credentials
// const PAYHERE_CONFIG = {
//     merchant_id: "1234435", // Replace with your Merchant ID
//     merchant_secret: "MzU4NTk2MzAxMzE4NzM1Njk5ODUxMTg1ODIwODU5ODM0NDY5MTUz", // Replace with your Merchant Secret
//     sandbox: false, // Set to false for production
//     // Update these URLs with your actual domain
//     return_url: "http://127.0.0.1:5501/pages/succsess.html",
//     cancel_url: "http://127.0.0.1:5501/pages/succsess.html",
//     notify_url:
//         "http://localhost:63342/AgroSmart/AgroSmart_System-Frontend/pages/paymentgatway.html",
// };

// ======================================================
let selectedGateway = null;

// Navbar scroll effect
window.addEventListener("scroll", function () {
    const navbar = document.querySelector(".navbar");
    if (window.scrollY > 50) {
        navbar.classList.add("scrolled");
    } else {
        navbar.classList.remove("scrolled");
    }
});

// ========================================
document.addEventListener("DOMContentLoaded", init);

function init() {
    console.log("Page started!");
    lodeCostomerAppoinmantDeatiles();
}
//=========================================

function lodeCostomerAppoinmantDeatiles() {
    $.ajax({
        url: "http://localhost:8080/api/v1/dentalcare/auth/getTemporarydata",
        method: "GET",
        contentType: "application/json",
        success: function (data, textStatus, jqXHR) {
            const custormerDeatiles = data.data;
            const appointmentObject = {
                appointmentDate: custormerDeatiles.pationEmail,
                email: custormerDeatiles.pationEmail,
                status: custormerDeatiles.status,
                description: custormerDeatiles.description,
                doctorId: custormerDeatiles.doctorId,
                paymentAmount: "2500",
            };

            localStorage.setItem(
                "appointmentObject",
                JSON.stringify(appointmentObject),
            );
        },
        error: function (jqXHR) {
            alert("error 777 lodeCostomerAppoinmantDeatiles");
        },
    });
}

// Select Gateway Function
function selectGateway(gateway) {
    selectedGateway = gateway;

    // Remove selected class from both
    document.getElementById("gateway-payhere").classList.remove("selected");
    document.getElementById("gateway-hnb").classList.remove("selected");

    // Add selected class to chosen gateway
    if (gateway === "payhere") {
        document.getElementById("gateway-payhere").classList.add("selected");
        document.getElementById("selectedGatewayText").innerHTML =
            '<i class="bi bi-check-circle-fill text-success"></i> Selected: PayHere (Credit/Debit Cards, Digital Wallets)';

        // Enable pay button
        document.getElementById("payButton").disabled = false;
        document.getElementById("payButton").style.opacity = "1";
    } else {
        document.getElementById("gateway-hnb").classList.add("selected");
        document.getElementById("selectedGatewayText").innerHTML =
            '<i class="bi bi-check-circle-fill text-success"></i> Selected: HNB Online (Direct Bank Transfer)';

        // Enable pay button
        document.getElementById("payButton").disabled = false;
        document.getElementById("payButton").style.opacity = "1";
    }
}

// Generate hash function for PayHere
function generatePayHereHash(orderId, amount) {
    try {
        // According to PayHere documentation:
        // hash = strtoupper(md5(merchant_id + order_id + amount + currency + strtoupper(md5(merchant_secret))))

        // Step 1: Generate MD5 of merchant_secret and convert to uppercase
        const secretMd5 = CryptoJS.MD5(PAYHERE_CONFIG.merchant_secret)
            .toString()
            .toUpperCase();

        // Step 2: Format amount to 2 decimal places without commas
        const formattedAmount = parseFloat(amount).toFixed(2);

        // Step 3: Concatenate all values
        const rawString =
            PAYHERE_CONFIG.merchant_id +
            orderId +
            formattedAmount +
            "LKR" +
            secretMd5;

        // Step 4: Generate final MD5 hash and convert to uppercase
        const finalHash = CryptoJS.MD5(rawString).toString().toUpperCase();
        console.log("Hash generated successfully:", finalHash);
        return finalHash;
    } catch (error) {
        console.error("Error generating hash:", error);
        return null;
    }
}

// Process Payment Function
function processPayment() {
    if (!selectedGateway) {
        Swal.fire({
            title: "No Gateway Selected",
            text: "Please select a payment gateway to continue",
            icon: "warning",
            confirmButtonColor: "#1c2d81",
            confirmButtonText: "OK",
        });
        return;
    }

    if (selectedGateway === "payhere") {
        processPayHerePayment();
    } else if (selectedGateway === "hnb") {
        // Handle HNB payment
        Swal.fire({
            title: "HNB Online Payment",
            text: "You will be redirected to HNB payment gateway",
            icon: "info",
            confirmButtonColor: "#1c2d81",
            confirmButtonText: "Proceed",
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: "Demo Mode",
                    text: "HNB payment integration - Redirect to HNB portal",
                    icon: "info",
                    confirmButtonColor: "#1c2d81",
                });
                // window.location.href = 'your-hnb-payment-url';
            }
        });
    }
}

function processPayHerePayment() {
    // Show processing alert
    Swal.fire({
        title: "Initializing PayHere Payment",
        text: "Please wait while we prepare the secure payment gateway...",
        icon: "info",
        showConfirmButton: false,
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        },
    });
    // startPayment;
    // Generate a unique order ID
    const orderId =
        "ORDER_" + Date.now() + "_" + Math.floor(Math.random() * 1000);
    const amount = "1000.00"; // You can make this dynamic based on your appointment fee

    // Generate the hash
    const hash = generatePayHereHash(orderId, amount);

    if (!hash) {
        Swal.fire({
            title: "Error",
            text: "Failed to generate payment hash. Please try again.",
            icon: "error",
            confirmButtonColor: "#1c2d81",
        });
        return;
    }

    // Set up PayHere event handlers
    payhere.onCompleted = function onCompleted(orderId) {
        console.log("Payment completed. OrderID:", orderId);
        Swal.fire({
            title: "Payment Successful!",
            text:
                "Your payment has been processed successfully. Order ID: " + orderId,
            icon: "success",
            confirmButtonColor: "#1c2d81",
            confirmButtonText: "View Details",
        }).then(() => {
            // Redirect to success page
            window.location.href =
                PAYHERE_CONFIG.return_url + "?order_id=" + orderId + "&status=success";
        });
    };

    payhere.onDismissed = function onDismissed() {
        console.log("Payment dismissed");
        Swal.fire({
            title: "Payment Cancelled",
            text: "You have cancelled the payment. Please try again when you're ready.",
            icon: "info",
            confirmButtonColor: "#1c2d81",
            confirmButtonText: "OK",
        });
    };

    payhere.onError = function onError(error) {
        console.log("Error:", error);
        Swal.fire({
            title: "Payment Error",
            text:
                "An error occurred: " +
                error +
                ". Please try again or contact support.",
            icon: "error",
            confirmButtonColor: "#1c2d81",
            confirmButtonText: "OK",
        });
    };

    var payment = {
        sandbox: PAYHERE_CONFIG.sandbox,
        merchant_id: PAYHERE_CONFIG.merchant_id,
        return_url: PAYHERE_CONFIG.return_url,
        cancel_url: PAYHERE_CONFIG.cancel_url,
        notify_url: PAYHERE_CONFIG.notify_url,
        order_id: orderId,
        items: "Dental Appointment Booking",
        amount: "1000.00",
        currency: "LKR",
        hash: hash,
        first_name: "Kalana",
        last_name: "Methsara",
        email: "kalanamethsara53@gmail.com",
        phone: "0771234567",
        address: "Hapugala,Galle",
        city: "Galle",
        country: "Sri Lanka",
        delivery_address: "No. 46, Galle road, Kalutara South",
        delivery_city: "Kalutara",
        delivery_country: "Sri Lanka",
    };

    Swal.close();

    setTimeout(() => {
        console.log("Starting PayHere payment with params:", payment);
        payhere.startPayment(payment);
    }, 500);
}
document.getElementById("sinig").addEventListener("click", function () {
    Swal.fire({
        title: "Sign In",
        text: "This is a demo version. Sign in functionality will be available soon.",
        icon: "info",
        confirmButtonColor: "#1c2d81",
    });
});

document.getElementById("sing-Up").addEventListener("click", function () {
    Swal.fire({
        title: "Sign Up",
        text: "This is a demo version. Sign up functionality will be available soon.",
        icon: "info",
        confirmButtonColor: "#1c2d81",
    });
});

// Initialize pay button as disabled
document.getElementById("payButton").disabled = true;

// Add validation for configuration
document.addEventListener("DOMContentLoaded", function () {
    const appointmentData = JSON.parse(localStorage.getItem("appointmentObject"));
    if (
        PAYHERE_CONFIG.merchant_id === "121XXXX" ||
        PAYHERE_CONFIG.merchant_secret === "YOUR_MERCHANT_SECRET"
    ) {
        console.warn(
            "⚠️ Please update PAYHERE_CONFIG with your actual Merchant ID and Secret",
        );
    }
});
