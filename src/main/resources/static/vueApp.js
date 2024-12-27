const { createApp } = Vue

const baseUrl = "http://localhost:8787/coin/"

const mainContainer = {
    data() {
        return {
            coins:[],
            canSeeTranscations: false,
            formCoin: {
                isNew: true,
                name: '',
                price: '',
                quantity: '',
                title: 'Cadastrar nova transação',
                button: 'Cadastrar'
            },
            transactions: []
        }
    },
    mounted(){
         this.showAllCoins()
    },
    methods:{
        showAllCoins() {
            this.coins = [];

            axios.get(baseUrl)
              .then(response => {
                const groupedCoins = response.data.reduce((acc, item) => {
                  if (acc[item.name]) {
                    acc[item.name].quantity += item.quantity;
                  } else {
                    acc[item.name] = { ...item };
                  }
                  return acc;
                }, {});
                this.coins = Object.values(groupedCoins);
              });
        },
        showTransactions(name){
            this.transactions = {
                coinName: name,
                data: []
            }

            this.canSeeTranscations = true

            axios.get(baseUrl + name)
                .then(response => {
                    response.data.forEach(item => {
                        this.transactions.data.push({
                            id: item.id,
                            name: item.name,
                            price: item.price.toLocaleString('pt-br', { style: 'currency', currency: 'BRL' }),
                            quantity: item.quantity,
                            updateDateTime: this.formattedDate(item.updateDateTime)
                        })
                    })
                })
                .catch(function (error){
                    console.log(error)
                    toastr.error(error)
                })
        },
        saveCoin(){
            this.formCoin.name = this.formCoin.name.toUpperCase()
            this.formCoin.price =  this.formCoin.price.replace("R$", '')
                .replace(',', '.').trim()

            if(this.formCoin.name == '' || this.formCoin.price == '' || this.formCoin.quantity == '') {
                toastr.error('Todos os campos são obrigatórios.', 'Formulário')
                return
            }

            const self = this

            if(this.formCoin.isNew) {
                const coin = {
                    name: this.formCoin.name,
                    price: this.formCoin.price,
                    quantity: this.formCoin.quantity
                }

                axios.post(baseUrl, coin)
                    .then(function (response) {
                        toastr.success('Nova transação cadastrada com sucesso!', 'Formulário')
                    })
                    .catch(function (error) {
                        toastr.error('Não foi possível cadastrar uma nova transação.', 'Formulário')
                    })
                    .then(function () {
                        self.showAllCoins()
                        self.showTransactions(self.formCoin.name)
                        self.cleanForm()
                    })
            } else {
                const coin = {
                    id: this.formCoin.id,
                    name: this.formCoin.name,
                    price: this.formCoin.price,
                    quantity: this.formCoin.quantity
                }

                axios.put(baseUrl, coin)
                    .then(function (response){
                        toastr.success('Transação atualizada com sucesso!', 'Formulário')
                    })
                    .catch(function (error){
                        toastr.error('Não foi possível atualizar a transação.' + error, 'Formulário')
                    })
                    .then(function () {
                        self.showAllCoins()
                        self.showTransactions(self.formCoin.name)
                        self.cleanForm()
                    })
            }
        },
        removeTransaction(transaction){
          const self = this

          axios.delete(baseUrl + transaction.id)
              .then(function (response) {
                  toastr.success('Transação removida com sucesso!', 'Exclusão')
              })
              .catch(function (error){
                  toastr.error('Não foi possível remover as transação.'+ error, 'Exclusão')
              })
              .then(function (){
                  self.showAllCoins()
                  //self.showTransactions(transaction.name)
                  self.canSeeTranscations = false
                  self.cleanForm()
              })
        },
        editTransaction(transaction){
            this.formCoin = {
                isNew: false,
                id: transaction.id,
                name: transaction.name.toUpperCase(),
                price: transaction.price,
                quantity: transaction.quantity,
                title: 'Editar transação',
                button: 'Atualizar'
            }
        },
        cleanForm(){
            this.formCoin.isNew = true
            this.formCoin.name = ''
            this.formCoin.price = ''
            this.formCoin.quantity = ''
            this.formCoin.title = 'Cadastrar nova transação'
            this.formCoin.button = 'Cadastrar'
        },
        formattedDate(date) {
            const dateObj = new Date(date);
            return dateObj.toLocaleDateString("pt-BR");
         },
        validatePriceInput(event) {
            let input = event.target.value;

            // Permitir apenas números e uma única vírgula
            input = input.replace(/[^0-9,]/g, '').replace(/(,.*?),/g, '$1');

            // Validar o formato compatível com DECIMAL(15,8)
            const [integerPart, decimalPart] = input.split(',');

            if (integerPart && integerPart.length > 7) {
              // Limitar a parte inteira a 15 dígitos
              input = integerPart.slice(0, 7) + (decimalPart ? `,${decimalPart}` : '');
            }

            if (decimalPart && decimalPart.length > 8) {
              // Limitar a parte decimal a 8 dígitos
              input = `${integerPart},${decimalPart.slice(0, 8)}`;
            }

            // Atualizar o valor do campo
            this.formCoin.price = input;
        },
        validateQuantityInput(event) {
            let input = event.target.value;

            input = input.replace(/[^0-9.]/g, '');   // Remove tudo que não for número ou ponto
            input = input.replace(/\.+/g, '.');      // Permite apenas um ponto

            // Validar o formato compatível com DECIMAL(15,8)
            const [integerPart, decimalPart] = input.split(',');

            if (integerPart && integerPart.length > 7) {
              // Limitar a parte inteira a 15 dígitos
              input = integerPart.slice(0, 7) + (decimalPart ? `,${decimalPart}` : '');
            }

            if (decimalPart && decimalPart.length > 8) {
              // Limitar a parte decimal a 8 dígitos
              input = `${integerPart},${decimalPart.slice(0, 8)}`;
            }

            // Atualizar o valor do campo
            this.formCoin.quantity = input;
        },
        validateNameInput(event) {
            let input = event.target.value;

            if (input.length > 100) {
              input = input.substring(0, 100);
            }

            this.formCoin.name = input.toUpperCase();
         },
    }
}

createApp(mainContainer).mount('#app')