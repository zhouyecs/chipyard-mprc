package chipyard

import org.chipsalliance.cde.config.{Config}

// ---------------------
// BOOM V3 Configs
// Performant, stable baseline
// ---------------------

class SmallBoomV3Config extends Config(
  new boom.v3.common.WithNSmallBooms(1) ++                             // small boom config
  new chipyard.config.AbstractConfig)

class MediumBoomV3Config extends Config(
  new boom.v3.common.WithNMediumBooms(1) ++                         // medium boom config
  new chipyard.config.AbstractConfig)


// // dev_yz_no_ras
// class NoRASx32 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMyMediumBooms(1) ++
//   new chipyard.config.AbstractConfig)


// // dev_yz_ras_tos_repair
// class RASTosRepairx8 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzRASTosRepairMediumBooms(1,8) ++
//   new chipyard.config.AbstractConfig)

// class RASTosRepairx16 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzRASTosRepairMediumBooms(1,16) ++
//   new chipyard.config.AbstractConfig)

// class RASTosRepairx32 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzRASTosRepairMediumBooms(1,32) ++
//   new chipyard.config.AbstractConfig)

// class RASTosRepairx64 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzRASTosRepairMediumBooms(1,64) ++
//   new chipyard.config.AbstractConfig)


// // dev_yz_saras
// class SARASx4y4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,4,4) ++
//   new chipyard.config.AbstractConfig)

// class SARASx8y4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,8,4) ++
//   new chipyard.config.AbstractConfig)

// class SARASx8y8 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,8,8) ++
//   new chipyard.config.AbstractConfig)

// class SARASx16y4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,16,4) ++
//   new chipyard.config.AbstractConfig)

// class SARASx16y8 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,16,8) ++
//   new chipyard.config.AbstractConfig)

// class SARASx16y16 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,16,16) ++
//   new chipyard.config.AbstractConfig)

// class SARASx32y4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,32,4) ++
//   new chipyard.config.AbstractConfig)

// class SARASx32y8 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,32,8) ++
//   new chipyard.config.AbstractConfig)

// class SARASx32y16 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,32,16) ++
//   new chipyard.config.AbstractConfig)

// class SARASx32y32 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,32,32) ++
//   new chipyard.config.AbstractConfig)

// class SARASx4y8 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySARASMediumBooms(1,4,8) ++
//   new chipyard.config.AbstractConfig)

// class SARASx4y16 extends Config(
// 	new boom.v3.common.UseLoopConfig(true) ++
// 	new boom.v3.common.WithMySARASMediumBooms(1,4,16) ++
// 	new chipyard.config.AbstractConfig)

// class SARASx8y16 extends Config(
// 	new boom.v3.common.UseLoopConfig(true) ++
// 	new boom.v3.common.WithMySARASMediumBooms(1,8,16) ++
// 	new chipyard.config.AbstractConfig)

// class SARASx4y32 extends Config(
// 	new boom.v3.common.UseLoopConfig(true) ++
// 	new boom.v3.common.WithMySARASMediumBooms(1,4,32) ++
// 	new chipyard.config.AbstractConfig)

// class SARASx8y32 extends Config(
// 	new boom.v3.common.UseLoopConfig(true) ++
// 	new boom.v3.common.WithMySARASMediumBooms(1,8,32) ++
// 	new chipyard.config.AbstractConfig)

// class SARASx16y32 extends Config(
// 	new boom.v3.common.UseLoopConfig(true) ++
// 	new boom.v3.common.WithMySARASMediumBooms(1,16,32) ++
// 	new chipyard.config.AbstractConfig)


// // dev_yz_dsras
// class DSRASx4y4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzDSRASMediumBooms(1,4) ++
//   new chipyard.config.AbstractConfig)

// class DSRASx8y8 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzDSRASMediumBooms(1,8) ++
//   new chipyard.config.AbstractConfig)

// class DSRASx16y16 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzDSRASMediumBooms(1,16) ++
//   new chipyard.config.AbstractConfig)

// class DSRASx32y32 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMydevyzDSRASMediumBooms(1,32) ++
//   new chipyard.config.AbstractConfig)


// // SQRAS
// class SQRASx4y4z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,4,4,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx8y4z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,8,4,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx4y8z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,4,8,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx8y8z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,8,8,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx8y16z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,8,16,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx16y8z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,16,8,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx16y16z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,16,16,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx32y16z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,32,16,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx16y32z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,16,32,4) ++
//   new chipyard.config.AbstractConfig)

// class SQRASx32y32z4 extends Config(
//   new boom.v3.common.UseLoopConfig(true) ++
//   new boom.v3.common.WithMySQRASMediumBooms(1,32,32,4) ++
//   new chipyard.config.AbstractConfig)


class MediumBoomV3SimConfig extends Config(
  new boom.v3.common.UseLoopConfig(true) ++
	new boom.v3.common.WithSimBooms(1) ++
	new chipyard.config.AbstractConfig)

class LargeBoomV3Config extends Config(
  new boom.v3.common.WithNLargeBooms(1) ++                          // large boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class MegaBoomV3Config extends Config(
  new boom.v3.common.WithNMegaBooms(1) ++                           // mega boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class DualSmallBoomV3Config extends Config(
  new boom.v3.common.WithNSmallBooms(2) ++                          // 2 boom cores
  new chipyard.config.AbstractConfig)

class Cloned64MegaBoomV3Config extends Config(
  new boom.v3.common.WithCloneBoomTiles(63, 0) ++
  new boom.v3.common.WithNMegaBooms(1) ++                           // mega boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class LoopbackNICLargeBoomV3Config extends Config(
  new chipyard.harness.WithLoopbackNIC ++                        // drive NIC IOs with loopback
  new icenet.WithIceNIC ++                                       // build a NIC
  new boom.v3.common.WithNLargeBooms(1) ++
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class MediumBoomV3CosimConfig extends Config(
  new chipyard.harness.WithCospike ++                            // attach spike-cosim
  new chipyard.config.WithTraceIO ++                             // enable the traceio
  new boom.v3.common.WithNMediumBooms(1) ++
  new chipyard.config.AbstractConfig)

class dmiCheckpointingMediumBoomV3Config extends Config(
  new chipyard.config.WithNPMPs(0) ++                            // remove PMPs (reduce non-core arch state)
  new chipyard.harness.WithSerialTLTiedOff ++                    // don't attach anything to serial-tl
  new chipyard.config.WithDMIDTM ++                              // have debug module expose a clocked DMI port
  new boom.v3.common.WithNMediumBooms(1) ++
  new chipyard.config.AbstractConfig)

class dmiMediumBoomV3CosimConfig extends Config(
  new chipyard.harness.WithCospike ++                            // attach spike-cosim
  new chipyard.config.WithTraceIO ++                             // enable the traceio
  new chipyard.harness.WithSerialTLTiedOff ++                    // don't attach anythint to serial-tl
  new chipyard.config.WithDMIDTM ++                              // have debug module expose a clocked DMI port
  new boom.v3.common.WithNMediumBooms(1) ++
  new chipyard.config.AbstractConfig)

class SimBlockDeviceMegaBoomV3Config extends Config(
  new chipyard.harness.WithSimBlockDevice ++                     // drive block-device IOs with SimBlockDevice
  new testchipip.iceblk.WithBlockDevice ++                       // add block-device module to peripherybus
  new boom.v3.common.WithNMegaBooms(1) ++                        // mega boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

// ---------------------
// BOOM V4 Configs
// Less stable and performant, but with more advanced micro-architecture
// Use for PD exploration
// ---------------------

class SmallBoomV4Config extends Config(
  new boom.v4.common.WithNSmallBooms(1) ++                          // small boom config
  new chipyard.config.AbstractConfig)

class MediumBoomV4Config extends Config(
  new boom.v4.common.WithNMediumBooms(1) ++                         // medium boom config
  new chipyard.config.AbstractConfig)

class LargeBoomV4Config extends Config(
  new boom.v4.common.WithNLargeBooms(1) ++                          // large boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class MegaBoomV4Config extends Config(
  new boom.v4.common.WithNMegaBooms(1) ++                           // mega boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class DualSmallBoomV4Config extends Config(
  new boom.v4.common.WithNSmallBooms(2) ++                          // 2 boom cores
  new chipyard.config.AbstractConfig)

class Cloned64MegaBoomV4Config extends Config(
  new boom.v4.common.WithCloneBoomTiles(63, 0) ++
  new boom.v4.common.WithNMegaBooms(1) ++                           // mega boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)

class MediumBoomV4CosimConfig extends Config(
  new chipyard.harness.WithCospike ++                            // attach spike-cosim
  new chipyard.config.WithTraceIO ++                             // enable the traceio
  new boom.v4.common.WithNMediumBooms(1) ++
  new chipyard.config.AbstractConfig)

class dmiCheckpointingMediumBoomV4Config extends Config(
  new chipyard.config.WithNPMPs(0) ++                            // remove PMPs (reduce non-core arch state)
  new chipyard.harness.WithSerialTLTiedOff ++                    // don't attach anything to serial-tl
  new chipyard.config.WithDMIDTM ++                              // have debug module expose a clocked DMI port
  new boom.v4.common.WithNMediumBooms(1) ++
  new chipyard.config.AbstractConfig)

class dmiMediumBoomV4CosimConfig extends Config(
  new chipyard.harness.WithCospike ++                            // attach spike-cosim
  new chipyard.config.WithTraceIO ++                             // enable the traceio
  new chipyard.harness.WithSerialTLTiedOff ++                    // don't attach anythint to serial-tl
  new chipyard.config.WithDMIDTM ++                              // have debug module expose a clocked DMI port
  new boom.v4.common.WithNMediumBooms(1) ++
  new chipyard.config.AbstractConfig)

class SimBlockDeviceMegaBoomV4Config extends Config(
  new chipyard.harness.WithSimBlockDevice ++                     // drive block-device IOs with SimBlockDevice
  new testchipip.iceblk.WithBlockDevice ++                       // add block-device module to peripherybus
  new boom.v4.common.WithNMegaBooms(1) ++                        // mega boom config
  new chipyard.config.WithSystemBusWidth(128) ++
  new chipyard.config.AbstractConfig)
