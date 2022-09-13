/*
See on github: https://github.com/muhammederdem/credit-card-form
*/

new Vue({
    el: "#app",
    data() {
      return {
        currentCardBackground: Math.floor(Math.random()* 25 + 1), // just for fun :D
        cardName: "",
        cardNumber: "",
        cardMonth: "",
        cardYear: "",
        cardCvv: "",
        minCardYear: new Date().getFullYear(),
        amexCardMask: "#### ###### #####",
        otherCardMask: "#### #### #### ####",
        cardNumberTemp: "",
        isCardFlipped: false,
        focusElementStyle: null,
        isInputFocused: false,
        description:"",
        amount: 0
      };
    },
    mounted() {
      this.cardNumberTemp = this.otherCardMask;
      document.getElementById("cardNumber").focus();
    },
    computed: {
      getCardType () {
        let number = this.cardNumber;
        let re = new RegExp("^4");
        if (number.match(re) != null) return "visa";
  
        re = new RegExp("^(34|37)");
        if (number.match(re) != null) return "amex";
  
        re = new RegExp("^5");
        if (number.match(re) != null) return "mastercard";
  
        re = new RegExp("^6011");
        if (number.match(re) != null) return "discover";
        
        re = new RegExp('^9792')
        if (number.match(re) != null) return 'troy'
  
        return "visa"; // default type
      },
          generateCardNumberMask () {
              return this.getCardType === "amex" ? this.amexCardMask : this.otherCardMask;
      },
      minCardMonth () {
        if (this.cardYear === this.minCardYear) return new Date().getMonth() + 1;
        return 1;
      }
    },
    watch: {
      cardYear () {
        if (this.cardMonth < this.minCardMonth) {
          this.cardMonth = "";
        }
      }
    },
    methods: {
      flipCard (status) {
        this.isCardFlipped = status;
      },
      focusInput (e) {
        this.isInputFocused = true;
        let targetRef = e.target.dataset.ref;
        let target = this.$refs[targetRef];
        this.focusElementStyle = {
          width: `${target.offsetWidth}px`,
          height: `${target.offsetHeight}px`,
          transform: `translateX(${target.offsetLeft}px) translateY(${target.offsetTop}px)`
        }
      },
      blurInput() {
        let vm = this;
        setTimeout(() => {
          if (!vm.isInputFocused) {
            vm.focusElementStyle = null;
          }
        }, 300);
        vm.isInputFocused = false;
      },
      transaction(){
        console.log(this.cardName,this.cardNumber,this.cardMonth,this.cardYear,this.cardCvv);
        let arr = this.cardNumber.split(" ")
        let number = ""
        for (let i = 0; i < arr.length; i++) {
            number += "-"+arr[i]            
        }
        console.log(number.substring(1));
        axios.post("/api/payments", {
            "number": number.substring(1),
            "cvv": this.cardCvv,
            "amount": this.amount,
            "description": this.description
        })
        .then(response => {
            Swal.fire({
                icon: 'success',
                title: 'Pago realizado satisfactoriamente',
                confirmButtonText: 'Ok',
              }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "/accounts.html"    
                }
              })
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: error.response.data,
                confirmButtonText: 'Vuelve a intentar'
              })
        });
      }
    }
  });
  